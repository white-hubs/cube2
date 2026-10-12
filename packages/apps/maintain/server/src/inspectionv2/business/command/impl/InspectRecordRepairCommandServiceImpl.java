package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordRepairCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordRepairMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRepairRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRepairStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairBatchCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairBatchTransferParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordRepairPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRepairRelationPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 巡检记录修复
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordRepairCommandServiceImpl extends ServiceImpl<InspectRecordRepairMapper, InspectRecordRepairPO> implements InspectRecordRepairCommandService {

    private final InspectRecordMapper inspectRecordMapper;
    private final HandlerService handlerService;
    private final FileRefClient fileRefClient;
    private final InspectRepairRelationMapper inspectRepairRelationMapper;
    private final UserClient userClient;
    private final MessageSendClient messageSendClient;

    /**
     * 批量提交巡检记录修复
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    public Boolean batchSubmitHandle(@RequestBody InspectRepairBatchCommand param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getRecordIdList()), "记录不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getRepairStatus()), "状态不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getContent()), "内容不能为空");
        Principal principal = SecurityContextHolder.getPrincipal();
        List<FileRefCreateParam> fileInduceParamList = new ArrayList<>();
        List<InspectRecordRepairPO> recordRepairPOList = new ArrayList<>();
        param.getRecordIdList().forEach(recordId -> {
            InspectRecordRepairPO po = new InspectRecordRepairPO();
            po.setId(IdWorker.getId());
            po.setRecordId(recordId);
            po.setRepairStatus(param.getRepairStatus());
            po.setContent(param.getContent());
            po.setUserId(principal.getId());
            po.setUserName(principal.getName());
            recordRepairPOList.add(po);
            if (CollectionUtils.isNotEmpty(param.getFileIdList())) {
                FileRefCreateParam fileInduceParam = new FileRefCreateParam();
                fileInduceParam.setFileIdList(param.getFileIdList());
                fileInduceParam.setBusinessKey(po.getId());
                fileInduceParam.setSceneCode(MaintainFileSceneCodeEnum.INSPECTION_RECORD.getCode());
                fileInduceParamList.add(fileInduceParam);
            }
        });
        if (CollectionUtils.isNotEmpty(recordRepairPOList)) {
            fileRefClient.refInduceBatch(fileInduceParamList);
        }
        this.saveBatch(recordRepairPOList);

        if (InspectRepairStatusEnum.WAIT_REPAIR.name().equals(param.getRepairStatus())) {
            // 如果是待修复就不用更新记录和任务状态
            return true;
        }
        // 更新巡检记录状态
        inspectRecordMapper.updateStatusByIds(param.getRecordIdList(), InspectRecordPO.builder().status(InspectRecordStatusEnum.OVER)
                .repairStatus(InspectRepairStatusEnum.REPAIRED.name()).build(), LocalDateTime.now(), principal.getAccount());

        /* 处理待办状态 */
        var inspectRecordPOList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                         .in(InspectRecordPO::getId, param.getRecordIdList())
                                                                         .groupBy(InspectRecordPO::getTaskId));
        Optional.ofNullable(inspectRecordPOList).orElse(CollectionUtils.emptyList())
                .forEach(inspectRecordPO -> handlerService.sendChangeTodo(inspectRecordPO.getId(), InspectTaskStatusEnum.REPAIRED, InspectTaskStatusEnum.WAIT_REPAIR.name(), new HashMap<>(), principal.getId(), Collections.singletonList(principal.getId())));
        return true;
    }

    /**
     * 批量取消修复
     *
     * @param param 记录id集合
     *
     * @return 是否成功
     */
    @Override
    public Boolean batchCleanRepair(@RequestBody InspectRepairBatchCommand param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getRecordIdList()), "记录不能为空");
        Principal principal = SecurityContextHolder.getPrincipal();
        Long cancelFileKey = IdWorker.getId();
        /* 持久化文件 */
        if (CollectionUtils.isNotEmpty(param.getFileIdList())) {
            List<FileRefCreateParam> fileInduceParamList = new ArrayList<>();
            param.getFileIdList().forEach(id -> {
                FileRefCreateParam fileInduceParam = new FileRefCreateParam();
                fileInduceParam.setFileIdList(param.getFileIdList());
                fileInduceParam.setBusinessKey(cancelFileKey);
                fileInduceParam.setSceneCode(MaintainFileSceneCodeEnum.INSPECTION_RECORD.getCode());
                fileInduceParamList.add(fileInduceParam);
            });
            fileRefClient.refInduceBatch(fileInduceParamList);
        }

        /* 更新记录 */
        inspectRecordMapper.updateStatusByIds(param.getRecordIdList(), InspectRecordPO.builder()
                .status(InspectRecordStatusEnum.CANCEL_REPAIR)
                .repairStatus(InspectRepairStatusEnum.CANCEL_REPAIR.name()).cancelReason(param.getContent()).cancelFileKey(cancelFileKey)
                .build(), LocalDateTime.now(), principal.getAccount());
        /* 处理待办状态 */
        var inspectRecordPOList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                         .in(InspectRecordPO::getId, param.getRecordIdList())
                                                                         .groupBy(InspectRecordPO::getTaskId));
        Optional.ofNullable(inspectRecordPOList).orElse(CollectionUtils.emptyList())
                .forEach(inspectRecordPO -> handlerService.sendChangeTodo(inspectRecordPO.getId(), InspectTaskStatusEnum.OVER, InspectTaskStatusEnum.WAIT_REPAIR.name(), new HashMap<>(), principal.getId(), Collections.singletonList(principal.getId())));
        return true;
    }

    /**
     * 批量取消修复
     *
     * @param param 记录id集合
     *
     * @return 是否成功
     */
    @Override
    public Boolean batchCleanRepairByTaskIds(@RequestBody InspectRepairBatchCommand param) {
        List<InspectRecordPO> recordPOList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                                    .select(InspectRecordPO::getId)
                                                                                    .in(InspectRecordPO::getTaskId, param.getTaskIdList()));
        if (CollectionUtils.isNotEmpty(recordPOList)) {
            param.setRecordIdList(recordPOList.stream().map(InspectRecordPO::getId).collect(Collectors.toList()));
            return this.batchCleanRepair(param);
        }
        return true;
    }

    /**
     * 修复转交
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    public Boolean transferRepair(@RequestBody InspectRepairBatchTransferParam param) {
        var user = userClient.getUser(param.getUserId());
        Assert.isNotFound(user, "用户不存在");
        var inspectRecordList = inspectRecordMapper.selectList(Wrappers.<InspectRecordPO>lambdaQuery()
                                                                       .in(InspectRecordPO::getId, param.getIds()));
        Assert.isNotFound(inspectRecordList == null || inspectRecordList.size() != param.getIds().size(), "记录不存在");
        var loginUserId = SecurityContextHolder.getPrincipal().getId();
        var loginUser = userClient.getUser(param.getUserId());
        var repairRelationPOList = inspectRepairRelationMapper.selectList(Wrappers.<InspectRepairRelationPO>lambdaQuery()
                                                                                  .eq(InspectRepairRelationPO::getUserId, loginUserId)
                                                                                  .in(InspectRepairRelationPO::getId, param.getIds()));
        Assert.isConflict(repairRelationPOList == null || repairRelationPOList.size() != param.getIds().size(), "不是自己的修复对象");
        // 修改修复对象的负责人
        inspectRepairRelationMapper.update(Wrappers.<InspectRepairRelationPO>lambdaUpdate()
                                                   .set(InspectRepairRelationPO::getUserId, param.getUserId())
                                                   .set(InspectRepairRelationPO::getNickname, user.getName())
                                                   .set(InspectRepairRelationPO::getOrganizationId,
                                                        user.getOrganization() != null ? user.getOrganization().getId() : null)
                                                   .set(InspectRepairRelationPO::getOrganizationName,
                                                        user.getOrganization() != null ? user.getOrganization().getName() : null)
                                                   .in(InspectRepairRelationPO::getId, param.getIds())
                                          );
        // 发送待办
        inspectRecordList
                .forEach((inspectRecord) -> {

                    handlerService.sendChangeTodo(inspectRecord.getId(), InspectTaskStatusEnum.WAIT_REPAIR, InspectTaskStatusEnum.WAIT_REPAIR.name(), new HashMap<>(), null, Collections.singletonList(param.getUserId()));
                    // 发送消息
                    var tempParam = new HashMap<String, String>();
                    tempParam.put("userName", loginUser.getName());
                    tempParam.put("targetName", inspectRecord.getTargetName());
                    messageSendClient.sceneSend(MessageSceneSendRequest.builder()
                                                        .msgSceneCode("INSPECT_REPAIR_TRANSFER")
                                                        .tenant(SessionContextHolder.getRequester().getTenant())
                                                        .businessId(String.valueOf(inspectRecord.getId()))
                                                        .tmpParams(tempParam)
                                                        .messageNotifyTargets(Collections.singletonList(MessageNotifyTarget.builder()
                                                                                                                .notifyTargetType(MessageNotifyTargetEnum.USER)
                                                                                                                .ids(Collections.singleton(param.getUserId()))
                                                                                                                .build()))
                                                        .build());
                });

        return true;
    }

}
