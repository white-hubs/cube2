package com.guhecloud.cube.base.message2.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecord;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTarget;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryByTenantParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTargetService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordApplicationTotalVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordTargetVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordTypeTotalVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordVO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.EventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.dto.MsgEventDataDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendMessage;
import com.guhecloud.cube.base.message2.server.handler.handler.send.site.rocketmq.message.WebSocketSendUser;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TerminalDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.TerminalQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息通道接口
 *
 * @author lanchun
 * @date 2021/11/10 13:56
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgRecordController {

    private final MsgRecordService msgRecordService;

    private final MsgRecordTargetService msgRecordTargetService;

    private final UserClient userClient;

    private final TenantClient tenantClient;

    private final TerminalClient terminalClient;

    /**
     * 消息列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/msg-records")
    public Pagination<MsgRecordPageVO> listMsgRecords(MsgRecordQueryParam param) {
        Page<MsgRecordPageVO> page = msgRecordService.page(param);
        List<MsgRecordPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), MsgRecordPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 管理端消息列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/admin/msg-records")
    public Pagination<MsgRecordPageVO> adminListMsgRecords(MsgRecordQueryByTenantParam param) {
        Page<MsgRecordPageVO> page = msgRecordService.page(param);
        List<MsgRecordPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), MsgRecordPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 消息详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/msg-record/{id}")
    public MsgRecordVO getMsgRecord(@PathVariable("id") Long id) {
        MsgRecord msgRecord = this.msgRecordService.getById(id);
        Assert.isNotFound(msgRecord, "消息不存在");
        //查询该消息的用户是否存在于这条消息中
        Principal principal = SecurityContextHolder.getPrincipal();
        MsgRecordTarget msgRecordTarget = msgRecordTargetService.getByRecordId(id, principal.getId());
        Assert.isNotFound(msgRecordTarget, "消息不存在");
        Assert.isNotAcceptable(msgRecordTarget.getSendStatus() == null || !msgRecordTarget.getSendStatus(), "消息发送中");
        //标记该条消息为已读
        if (!msgRecordTarget.getHasRead()) {
            msgRecordTarget.setHasRead(true);
            msgRecordTargetService.updateById(msgRecordTarget);
            sendUserUnReadMsgRefreshSite();
        }
        LocalDateTime dateTime = DateUtils.now();
        //消息是否过期
        if (Objects.nonNull(msgRecord.getExpirationTime()) && dateTime.isAfter(msgRecord.getExpirationTime())) {
            throw new ResourceNotFoundException("消息已过期");
        }
        MsgRecordVO msgRecordVO = ObjectUtils.copying(msgRecord, MsgRecordVO.class);
        msgRecordVO.setSendTime(msgRecordTarget.getSendTime());
        if (Objects.nonNull(msgRecordTarget.getSourceTenant())) {
            TenantDTO tenantDTO = tenantClient.getByCode(msgRecordTarget.getSourceTenant());
            if (Objects.nonNull(tenantDTO)) {
                msgRecordVO.setSourceTenantName(tenantDTO.getName());
            }
        }
        return msgRecordVO;
    }

    /**
     * 消息类型统计未读数量
     *
     * @param channelType 通道编号
     *
     * @return
     */
    @GetMapping("/msg-record/type/{channelType}/statistics")
    public List<MsgRecordTypeTotalVO> msgTypeUnreadTotal(@PathVariable("channelType") ChannelEnum channelType) {
        return this.msgRecordService.msgTypeUnreadTotal(channelType);
    }

    /**
     * 消息应用统计未读数量
     *
     * @param channelType
     *
     * @return
     */
    @GetMapping("/msg-record/application/{channelType}/statistics")
    public List<MsgRecordApplicationTotalVO> msgApplicationUnreadTotal(@PathVariable("channelType") ChannelEnum channelType) {
        return this.msgRecordService.msgApplicationUnreadTotal(channelType);
    }

    /**
     * 消息数量(未读)
     *
     * @param channelType 通道编号
     *
     * @return
     */
    @GetMapping("/msg-record/un-read/total/{channelType}")
    public Long msgUnReadTotal(@PathVariable("channelType") ChannelEnum channelType) {
        return this.msgRecordTargetService.unReadTotal(channelType);
    }

    /**
     * 消息一键已读
     *
     * @param ChannelEnum
     */
    @PutMapping("/msg-record/read-all/{channelType}")
    public void readAll(@NotNull(message = "通道类型不能为空") @PathVariable("channelType") ChannelEnum ChannelEnum) {
        this.msgRecordTargetService.readAll(ChannelEnum);
        sendUserUnReadMsgRefreshSite();
    }

    /**
     * 消息类型一键已读
     *
     * @param ChannelEnum
     * @param messageTypeEnum
     */
    @PutMapping("/msg-record/read-all-msg-type/{channelType}/{msgType}")
    public void readAllMsgType(@NotNull(message = "通道类型不能为空") @PathVariable("channelType") ChannelEnum ChannelEnum,
                               @NotNull(message = "消息类型不能为空") @PathVariable("msgType") String messageTypeEnum
                              ) {
        this.msgRecordTargetService.readAllMsgType(ChannelEnum, messageTypeEnum);
        sendUserUnReadMsgRefreshSite();
    }

    /**
     * 应用消息一键已读
     *
     * @param ChannelEnum
     * @param applicationCode
     */
    @PutMapping("/msg-record/read-all--application-msg/{channelType}/{applicationCode}")
    public void readAllApplicationMsg(@NotNull(message = "通道类型不能为空") @PathVariable("channelType") ChannelEnum ChannelEnum,
                                      @NotNull(message = "应用编号不能为空") @PathVariable("applicationCode") String applicationCode
                                     ) {
        this.msgRecordTargetService.readAllApplicationMsg(ChannelEnum, applicationCode);
        sendUserUnReadMsgRefreshSite();
    }

    /**
     * 消息批量已读
     *
     * @param ids 消息主键集合
     */
    @PutMapping("/msg-record/read-batch")
    public void readBatch(@Valid @RequestBody @Size(min = 1, message = "读取的消息数量不能小于1") List<Long> ids) {
        this.msgRecordTargetService.readBatch(ids);
        sendUserUnReadMsgRefreshSite();
    }

    /**
     * 消息删除
     *
     * @param id 消息主键
     */
    @DeleteMapping("/msg-record/{id}")
    public void remove(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1") @PathVariable("id") Long id) {
        this.msgRecordTargetService.remove(id);
    }

    /**
     * 消息批量删除
     *
     * @param ids 消息主键集合
     */
    @DeleteMapping("/msg-record/batch")
    public void removeBatch(@RequestParam(name = "ids") @NotNull(message = "消息主键不能为空") List<Long> ids) {
        this.msgRecordTargetService.removeBatch(ids);
    }

    /**
     * 根据业务主键批量删除消息
     *
     * @param businessIds 业务主键集合
     */
    @DeleteMapping("/msg-record/business/batch")
    public void removeBatchByBusinessIds(@RequestBody Set<String> businessIds) {
        if (!CollectionUtils.isEmpty(businessIds)) {
            this.msgRecordService.removeBatchByBusinessIds(businessIds);
        }
    }

    /**
     * 通过业务主键获取发送人信息列表
     *
     * @param businessKey 业务主键
     *
     * @return
     */
    @GetMapping("/msg-record/targets/{businessKey}")
    public List<MsgRecordTargetVO> listMsgRecordTargets(@PathVariable("businessKey") String businessKey) {
        List<MsgRecord> msgRecords = this.msgRecordService.listByBusinessId(businessKey);
        if (CollectionUtils.isEmpty(msgRecords)) {
            return Collections.emptyList();
        }
        Set<Long> msgRecordIds = msgRecords.stream().map(MsgRecord::getId).collect(Collectors.toSet());
        List<MsgRecordTarget> msgRecordTargets = this.msgRecordTargetService.listByMsgRecordIds(msgRecordIds);
        if (CollectionUtils.isEmpty(msgRecordTargets)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = msgRecordTargets.stream().map(MsgRecordTarget::getUserId).collect(Collectors.toSet());
        Map<Long, UserDTO> userDTOMap = this.userClient.postList(UserQuery.builder().userIds(userIds).build())
                .stream().collect(Collectors.toMap(UserDTO::getId, s -> s));
        List<MsgRecordTargetVO> msgRecordTargetVOS = ObjectUtils.copying(msgRecordTargets, MsgRecordTargetVO.class);
        msgRecordTargetVOS.forEach(msgRecordTargetVO -> {
            msgRecordTargetVO.setChannelTypeName(msgRecordTargetVO.getChannelCode().getDescription());
            if (userDTOMap.containsKey(msgRecordTargetVO.getUserId())) {
                msgRecordTargetVO.setUserName(userDTOMap.get(msgRecordTargetVO.getUserId()).getName());
            }
        });
        return msgRecordTargetVOS;
    }

    /**
     * 发送用户未读消息刷新站内信
     */
    private void sendUserUnReadMsgRefreshSite() {
        TerminalQueryParam param = new TerminalQueryParam();
        param.setPageNumber(1);
        param.setPageSize(1000);
        Pagination<TerminalDTO> terminalDTOPagination = terminalClient.listByPage(param);
        Set<String> terminalCode = terminalDTOPagination.getRecords().stream().map(TerminalDTO::getCode).collect(Collectors.toSet());
        EventDataDTO eventDataDTO = new EventDataDTO("USER_UN_READ_MESSAGE_REFRESH", "USER_UN_READ_MESSAGE_REFRESH");
        //消息体
        MsgEventDataDTO msgEventDataDTO = new MsgEventDataDTO("USER_UN_READ_MESSAGE_REFRESH", "terminal_tenant_person", eventDataDTO);
        List<WebSocketSendUser> webSocketSendUsers = terminalCode.stream()
                .map(terminal -> new WebSocketSendUser(SecurityContextHolder.getPrincipal().getId(),
                                                       terminal))
                .collect(Collectors.toList());
        WebSocketSendMessage webSocketSendMessage = new WebSocketSendMessage(msgEventDataDTO, webSocketSendUsers,
                                                                             SessionContextHolder.getRequester().getTenant(),
                                                                             Collections.emptySet());
        RocketMQContextHolder.send(WebSocketSendMessage.TOPIC, webSocketSendMessage);
    }
}
