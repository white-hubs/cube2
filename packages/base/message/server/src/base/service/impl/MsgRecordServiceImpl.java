package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgRecordMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecord;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTarget;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTerminal;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgSendStatusEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryByTenantParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryParam;
import com.guhecloud.cube.base.message2.server.base.rocketmq.message.MsgRecordMqMessage;
import com.guhecloud.cube.base.message2.server.base.rocketmq.message.MsgRecordTerminalMqMessage;
import com.guhecloud.cube.base.message2.server.base.rocketmq.message.MsgRecordUserMqMessage;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTargetService;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTerminalService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordApplicationTotalVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordTypeTotalVO;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息记录操作实现
 *
 * @author lanchun
 * @date 2021/11/9 19:23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgRecordServiceImpl extends ServiceImpl<MsgRecordMapper, MsgRecord> implements MsgRecordService {

    private final MsgRecordTargetService msgRecordTargetService;

    private final MsgRecordTerminalService msgRecordTerminalService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMsgRecordStatus(@NonNull Long id, @NonNull Boolean sendStatus, @NonNull LocalDateTime sendTime,
                                      List<MessageSendResponse.Receiver> failReceiverInfos
                                     ) {
        MsgRecord msgRecord = this.getById(id);
        if (Objects.nonNull(msgRecord)) {
            msgRecord.setSendStatus(sendStatus ? MsgSendStatusEnum.SEND_SUCCESSFULLY : MsgSendStatusEnum.SEND_FAIL);
            msgRecord.setExpirationTime(Objects.nonNull(msgRecord.getEffectiveDays())
                                        ? DateUtils.offset(sendTime, ChronoUnit.DAYS, msgRecord.getEffectiveDays())
                                        : null);
            msgRecord.setSendTime(sendTime);
            this.baseMapper.updateById(msgRecord);
            List<String> failReceivers = new ArrayList<>();
            //用户发送状态修改
            if (!CollectionUtils.isEmpty(failReceiverInfos)) {
                failReceiverInfos.stream().forEach(failReceiver -> {
                    failReceivers.add(failReceiver.getReceiver());
                    LambdaUpdateWrapper<MsgRecordTarget> failUserLambdaUpdateWrapper = Wrappers.<MsgRecordTarget>lambdaUpdate()
                            .eq(MsgRecordTarget::getMsgRecordId, id)
                            .eq(MsgRecordTarget::getReceiver, failReceiver.getReceiver())
                            .set(MsgRecordTarget::getSendStatus, false)
                            .set(MsgRecordTarget::getHasRead, false)
                            .set(MsgRecordTarget::getSendTime, sendTime)
                            .set(MsgRecordTarget::getErrorDescribe, failReceiver.getErrorDescribe());
                    this.msgRecordTargetService.update(failUserLambdaUpdateWrapper);
                });
            }
            //成功用户
            LambdaUpdateWrapper<MsgRecordTarget> successUserLambdaUpdateWrapper = Wrappers.<MsgRecordTarget>lambdaUpdate()
                    .isNull(MsgRecordTarget::getSendStatus)
                    .eq(MsgRecordTarget::getMsgRecordId, id)
                    .notIn(!CollectionUtils.isEmpty(failReceivers), MsgRecordTarget::getReceiver, failReceivers)
                    .set(MsgRecordTarget::getSendStatus, true)
                    .set(MsgRecordTarget::getHasRead, false)
                    .set(MsgRecordTarget::getSendTime, sendTime);
            this.msgRecordTargetService.update(successUserLambdaUpdateWrapper);
            //发送事务消息
            RocketMQContextHolder.send(
                    MsgRecordMqMessage.TOPIC,
                    this.buildMsgRecordMqMsg(msgRecord, failReceivers)
                                      );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMsgRecordTargetStatus(Long id, Boolean sendStatus, LocalDateTime sendTime, String errorDescribe) {
        LambdaUpdateWrapper<MsgRecordTarget> failUserLambdaUpdateWrapper = Wrappers.<MsgRecordTarget>lambdaUpdate()
                .eq(MsgRecordTarget::getId, id)
                .set(MsgRecordTarget::getSendStatus, sendStatus)
                .set(MsgRecordTarget::getSendTime, sendTime)
                .set(MsgRecordTarget::getErrorDescribe, errorDescribe);
        this.msgRecordTargetService.update(failUserLambdaUpdateWrapper);
    }

    @Override
    public MsgRecord getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<MsgRecord> listByBusinessId(@NonNull String businessId) {
        return this.lambdaQuery().eq(MsgRecord::getBusinessId, businessId).list();
    }

    @Override
    public List<MsgRecordTypeTotalVO> msgTypeUnreadTotal(@NonNull ChannelEnum channelType) {
        return this.baseMapper
                .selectMsgTypeUnreadTotal(channelType, SessionContextHolder.getRequester().getTerminal(), SecurityContextHolder
                        .getPrincipal().getId());
    }

    @Override
    public List<MsgRecordApplicationTotalVO> msgApplicationUnreadTotal(@NonNull ChannelEnum channelType) {
        List<MsgRecordApplicationTotalVO> msgApplications = this.baseMapper
                .selectMsgApplication(channelType, SessionContextHolder.getRequester().getTerminal(), SecurityContextHolder.getPrincipal()
                        .getId());
        List<MsgRecordApplicationTotalVO> msgRecordApplicationTotalVOS = this.baseMapper
                .selectMsgApplicationUnreadTotal(channelType, SessionContextHolder.getRequester().getTerminal(), SecurityContextHolder
                        .getPrincipal().getId(), null);
        msgApplications.stream().forEach(msgRecordApplication -> {
            MsgRecordApplicationTotalVO msgRecordApplicationTotalVO = msgRecordApplicationTotalVOS.stream()
                    .filter(msgRecordApplicationTotal -> msgRecordApplication.getApplicationCode()
                            .equals(msgRecordApplicationTotal.getApplicationCode())).findFirst().orElse(null);
            if (Objects.nonNull(msgRecordApplicationTotalVO)) {
                msgRecordApplication.setTotal(msgRecordApplicationTotalVO.getTotal());
            } else {
                msgRecordApplication.setTotal(0);
            }
        });
        return msgApplications;
    }

    @Override
    public Page<MsgRecordPageVO> page(MsgRecordQueryParam param) {
        Principal principal = SecurityContextHolder.getPrincipal();
        param.setSendStatus(Boolean.TRUE);
        return this.baseMapper
                .page(PagingUtils.convert(param), param, SessionContextHolder.getRequester().getTerminal(), principal.getId());
    }

    @Override
    public Page<MsgRecordPageVO> page(MsgRecordQueryByTenantParam param) {
        Principal principal = SecurityContextHolder.getPrincipal();
        param.setSendStatus(Boolean.TRUE);
        return this.baseMapper
                .pageByTenant(PagingUtils.convert(param), param, SessionContextHolder.getRequester().getTerminal(), principal.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeExpireDel() {
        //查询过期的消息列表
        List<MsgRecord> list = this.baseMapper.selectList(Wrappers.<MsgRecord>lambdaQuery()
                                                                  .le(MsgRecord::getExpirationTime, DateUtils.now()));
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> msgRecordIds = list.stream().map(MsgRecord::getId).collect(Collectors.toList());
            //物理删除
            this.baseMapper.deleteByIds(msgRecordIds);
            //物理删除发送对象
            this.msgRecordTargetService.remove(Wrappers.<MsgRecordTarget>lambdaQuery().in(MsgRecordTarget::getMsgRecordId, msgRecordIds));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        //物理删除
        this.baseMapper.deleteById(id);
        //物理删除发送对象
        this.msgRecordTargetService.remove(Wrappers.<MsgRecordTarget>lambdaQuery().eq(MsgRecordTarget::getMsgRecordId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByBusinessIds(@NonNull Set<String> businessIds) {
        List<MsgRecord> msgRecords = this.baseMapper.selectList(Wrappers.<MsgRecord>lambdaQuery()
                                                                        .select(MsgRecord::getId)
                                                                        .in(MsgRecord::getBusinessId, businessIds));
        if (CollectionUtils.isEmpty(msgRecords)) {
            return;
        }
        Set<Long> msgRecordIds = msgRecords.stream().map(MsgRecord::getId).collect(Collectors.toSet());
        this.msgRecordTargetService.removeBatchByBusinessIds(businessIds);
        this.msgRecordTerminalService.removeBatchByMsgRecordIds(msgRecordIds);
        DatabaseContextHolder.softDelete(MsgRecord.class, msgRecordIds);
    }

    private MsgRecordMqMessage buildMsgRecordMqMsg(MsgRecord msgRecord, List<String> failReceivers) {
        MsgRecordMqMessage msgRecordMqMessage = ObjectUtils.copying(msgRecord, MsgRecordMqMessage.class);
        List<MsgRecordTarget> msgRecordTargets = this.msgRecordTargetService
                .listByMsgRecordIds(Collections.singleton(msgRecordMqMessage.getId()));
        List<MsgRecordTerminal> msgRecordTerminals = this.msgRecordTerminalService.listMsgRecordTerminal(msgRecordMqMessage.getId());
        msgRecordMqMessage.setMsgRecordUser(ObjectUtils.copying(msgRecordTargets, MsgRecordUserMqMessage.class));
        msgRecordMqMessage.getMsgRecordUser().stream().forEach(msgRecordUser -> {
            if (failReceivers.contains(msgRecordUser.getReceiver())) {
                msgRecordUser.setSendStatus(false);
            } else {
                msgRecordUser.setSendStatus(true);
            }
        });
        msgRecordMqMessage.setMsgRecordTerminals(ObjectUtils.copying(msgRecordTerminals, MsgRecordTerminalMqMessage.class));
        return msgRecordMqMessage;
    }
}
