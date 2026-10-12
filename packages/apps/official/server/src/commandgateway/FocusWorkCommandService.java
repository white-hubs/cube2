package com.guhecloud.cube.srv.gen.official.management.server.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkCommand;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkFailCommand;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkReplyCommand;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkUpdateCommand;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkCategoryMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkFailMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkReplyMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkUpdateMapper;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.FocusWorkStatus;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkDeleteEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkPassEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkReplyEvent;
import com.guhecloud.cube.srv.gen.official.management.server.message.event.FocusWorkUpdateEvent;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkCategoryPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkFailPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkReplyPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkUpdatePO;
import com.guhecloud.cube.srv.gen.official.management.server.po.model.FlowCommonEntity;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author appet
 * @date 2023/02/21
 * @description 督办事项管理
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkCommandService extends ServiceImpl<FocusWorkMapper, FocusWorkPO> {
    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final FocusWorkCategoryMapper focusWorkCategoryMapper;
    private final FocusWorkReplyMapper focusWorkReplyMapper;
    private final FocusWorkUpdateMapper focusWorkUpdateMapper;
    private final FocusWorkMapper focusWorkMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FocusWorkFailMapper focusWorkFailMapper;
    private final FileOperateClient fileOperateClient;

    /**
     * 新增
     *
     * @param command
     *
     * @return
     */
    public Long addFocusWork(FocusWorkCommand command) {
        FocusWorkPO focusWork = ObjectUtils.copying(command, FocusWorkPO.class);
        //默认状态
        focusWork.setStatus(FocusWorkStatus.PROCESSING.name());
        this.save(extendFocusWork(focusWork));
        //消息提醒
        try {
            applicationEventPublisher.publishEvent(new FocusWorkPassEvent(this, focusWork));
        } catch (Exception e) {
            log.error("督办事项，消息推送异常：", e);
        }
        return focusWork.getId();
    }

    /**
     * 扩展督办工作的相关信息
     *
     * @param focusWork
     *
     * @return
     */
    public FocusWorkPO extendFocusWork(FocusWorkPO focusWork) {
        if (StringUtils.isNotEmpty(focusWork.getDeptId()) && StringUtils.isEmpty(focusWork.getDeptName())) {
            String deptNames = Arrays.stream(focusWork.getDeptId().split(",")).map(deptId -> {
                OrganizationDTO org = organizationClient.getOrganizationsById(Long.valueOf(deptId));
                return org != null ? org.getName() : "";
            }).collect(Collectors.joining(","));
            focusWork.setDeptName(deptNames);
        }
        if (StringUtils.isNotEmpty(focusWork.getFirstUserId()) && StringUtils.isEmpty(focusWork.getFirstUserName())) {
            String userNames = Arrays.stream(focusWork.getFirstUserId().split(",")).map(userId -> {
                UserDTO user = userClient.getUser(Long.valueOf(userId));
                return user != null ? user.getName() : "";
            }).collect(Collectors.joining(","));
            focusWork.setFirstUserName(userNames);
        }
        if (StringUtils.isNotEmpty(focusWork.getSecondUserId()) && StringUtils.isEmpty(focusWork.getSecondUserName())) {
            String userNames = Arrays.stream(focusWork.getSecondUserId().split(",")).map(userId -> {
                UserDTO user = userClient.getUser(Long.valueOf(userId));
                return user != null ? user.getName() : "";
            }).collect(Collectors.joining(","));
            focusWork.setSecondUserName(userNames);
        }
        if (ObjectUtils.isNotEmpty(focusWork.getLeadLeaderUserId())) {
            List<String> userIds = JsonUtils.deserialize(focusWork.getLeadLeaderUserId(), new TypeReference<List<String>>() {
            });
            focusWork.setLeadLeaderUserId(userIds.get(0));
            UserDTO user = userClient.getUser(Long.valueOf(focusWork.getLeadLeaderUserId()));
            focusWork.setLeadLeaderUserName(user != null ? user.getName() : "");
        }
        //下发人处理
        if (StringUtils.isNotEmpty(focusWork.getFocusWorkStarterId()) && StringUtils.isEmpty(focusWork.getFocusWorkStarter())) {
            String userNames = Arrays.stream(focusWork.getFocusWorkStarterId().split(",")).map(userId -> {
                UserDTO user = userClient.getUser(Long.valueOf(userId));
                return user != null ? user.getName() : "";
            }).collect(Collectors.joining(","));
            focusWork.setFocusWorkStarter(userNames);
        }
        if (focusWork.getWorkCategoryId() != null && StringUtils.isEmpty(focusWork.getWorkCategoryName())) {
            FocusWorkCategoryPO category = focusWorkCategoryMapper.getByIdInAll(focusWork.getWorkCategoryId());
            focusWork.setWorkCategoryName(category != null ? category.getName() : "");
        }
        //扩展申请人信息
        this.extendApplicantInfo(focusWork);

        return focusWork;
    }

    /**
     * 扩展申请人相关信息
     *
     * @param entity
     */
    private void extendApplicantInfo(FlowCommonEntity entity) {
        if (entity.getApplicantId() != null) {
            UserDTO user = userClient.getUser(entity.getApplicantId());
            if (user != null) {
                entity.setApplicantName(user.getName());
                entity.setApplicantDeptId(user.getOrganization().getId());
                entity.setApplicantDeptName(user.getOrganization().getName());
            }
        }
    }

    /**
     * 添加反馈 并更新督办工作相关事项
     *
     * @param command
     */
    public void addReplyLogAndUpdateFocusWork(FocusWorkReplyCommand command) {
        FocusWorkReplyPO focusReply = ObjectUtils.copying(command, FocusWorkReplyPO.class);
        this.extendApplicantInfo(focusReply);
        //插入反馈记录
        focusWorkReplyMapper.insert(focusReply);
        //更新督办工作信息
        FocusWorkPO focusWork = this.updateFocusWorkByReply(command);
        //更新代办
        try {
            applicationEventPublisher.publishEvent(new FocusWorkReplyEvent(this, focusWork, focusReply));
        } catch (Exception e) {
            log.error("督办事项，消息推送异常：", e);
        }
    }

    /**
     * 更新督办工作进度，并改变工作状态
     *
     * @param command
     */
    private FocusWorkPO updateFocusWorkByReply(FocusWorkReplyCommand command) {
        //ID查询督办工作
        FocusWorkPO focusWorkPO = this.focusWorkMapper.selectById(command.getFocusWorkId());
        focusWorkPO.setPercentage(command.getPercentage());
        if (command.getPercentage() >= 100) {//百分比大于等于100
            focusWorkPO.setStatus(FocusWorkStatus.FINISH.name());
        }
        focusWorkPO.setPracticableDetails(command.getPracticableDetails());
        this.focusWorkMapper.updateById(focusWorkPO);
        return focusWorkPO;
    }

    /**
     * 添加更新 并更新督办工作相关事项
     *
     * @param command
     */
    public void addUpdateLogAndUpdateFocusWork(FocusWorkUpdateCommand command) {
        FocusWorkUpdatePO update = ObjectUtils.copying(command, FocusWorkUpdatePO.class);
        this.extendApplicantInfo(update);
        //插入变更记录
        this.focusWorkUpdateMapper.insert(update);
        //更新督办工作信息
        this.updateFocusWorkByUpdate(command);
        //推送消息
        //消息提醒
        try {
            applicationEventPublisher.publishEvent(new FocusWorkUpdateEvent(this, update));
        } catch (Exception e) {
            log.error("督办事项，消息推送异常：", e);
        }
    }

    /**
     * 通过变更修改督办事项
     *
     * @param command
     */
    private void updateFocusWorkByUpdate(FocusWorkUpdateCommand command) {
        //ID查询督办工作
        FocusWorkPO focusWorkPO = this.focusWorkMapper.selectById(command.getFocusWorkId());
        focusWorkPO.setUpdateCompleteTime(command.getUpdateOverTime());
        focusWorkPO.setUpdateCompleteTimeStr(command.getUpdateOverTimeStr());
        if (FocusWorkStatus.PROCESSING_DELAY.name().equals(command.getStatus())) {
            focusWorkPO.setStatus(FocusWorkStatus.PROCESSING.name());
        } else {
            focusWorkPO.setStatus(command.getStatus());
        }
        this.focusWorkMapper.updateById(focusWorkPO);
    }

    /**
     * 添加督办事项最终的失败或未完成原因
     *
     * @param command
     */
    public void addFocusWorkFail(FocusWorkFailCommand command) {
        //这里是覆盖操作，如果已经有了，直接删除，然后再新建
        this.focusWorkFailMapper.delete(Wrappers.<FocusWorkFailPO>lambdaQuery()
                                                .eq(FocusWorkFailPO::getFocusWorkId, command.getFocusWorkId()));
        //新建
        FocusWorkFailPO failPO = ObjectUtils.copying(command, FocusWorkFailPO.class);
        failPO.setId(IdWorker.getId());
        //文件持久化
        if (StringUtils.isNotBlank(failPO.getFileIds())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdStr(failPO.getFileIds());
            fileInduceParam.setBusinessKey(failPO.getId());
            fileInduceParam.setSceneCode("FOCUS_WORK_FAIL");
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        this.focusWorkFailMapper.insert(failPO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void focusWorkBatchDelete(FocusWorkBatchDeleteCommand command) {
        if (CollectionUtils.isEmpty(command.getProcessInstanceIds())) {
            return;
        }
        List<FocusWorkPO> list = this.lambdaQuery().in(FocusWorkPO::getProcessInstanceId, command.getProcessInstanceIds()).list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        DatabaseContextHolder.softDelete(FocusWorkPO.class, list);
        this.applicationEventPublisher.publishEvent(
                new FocusWorkDeleteEvent(this,
                                         list.stream().map(FocusWorkPO::getId).collect(Collectors.toSet()),
                                         list.stream().map(FocusWorkPO::getProcessInstanceId).collect(Collectors.toSet())));
    }
}
