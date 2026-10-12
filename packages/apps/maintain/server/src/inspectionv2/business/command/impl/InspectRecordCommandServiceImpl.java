package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeObjDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAssociatedStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.videogw.client.VideoClient;
import com.guhecloud.cube.base.videogw.client.command.VideoSnapCaptureCommand;
import com.guhecloud.cube.base.videogw.client.dto.VideoSnapCaptureDTO;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.device.supervise.client.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.HandlerService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectRecordStatusChangeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectScenePointDeviceService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectTaskStatusChangeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanTargetReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl.InspectRecordQueryServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordToBusinessMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRepairRelationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectNoticeModuleTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRecordStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectRepairStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionMappingEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.filter.BusinessTypeInterceptor;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordAndTaskDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordSubmitDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.RecordTaskDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.SubmitInspectResultMessage;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordCancelBatchCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordToBusinessCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskRecordSubmitCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordToBusinessPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRepairRelationPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanTargetReferVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordCommandServiceImpl extends ServiceImpl<InspectRecordMapper, InspectRecordPO> implements InspectRecordCommandService {

    private final UserClient userClient;

    private final InspectPlanTargetReferQueryService planTargetReferQueryService;
    private final InspectRecordItemCommandService recordItemCommandService;
    private final InspectRecordToBusinessMapper recordToBusinessMapper;
    private final InspectTaskMapper taskMapper;
    private final InspectTaskStatusChangeService taskStatusChangeService;
    private final InspectRecordStatusChangeService recordStatusChangeService;
    private final InspectRecordQueryServiceImpl inspectRecordQueryService;
    private final FileRefClient fileRefClient;
    private final FileOperateClient fileOperateClient;
    private final AssemblyNoticeClient assemblyNoticeClient;
    private final InspectRecordItemCommandService inspectRecordItemCommandService;
    private final ApplicationClient applicationClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final DeviceCenterAssociatedStdClient deviceCenterAssociatedStdClient;
    private final VideoClient videoClient;
    private final HandlerService handlerService;
    private final InspectRepairRelationMapper repairRelationMapper;
    private final InspectScenePointDeviceService inspectScenePointDeviceService;
    @Resource(name = "SingleThreadPool")
    private ThreadPoolTaskExecutor singleThreadPool;

    @Override
    public void saveByTask(InspectTaskPO taskPO) {
        List<InspectPlanTargetReferVO> planTargetReferVOS = planTargetReferQueryService.getVOByPlanId(taskPO.getPlanId());
        Optional.ofNullable(planTargetReferVOS).orElse(Collections.emptyList())
                .forEach(planTargetReferVO -> {
                    InspectRecordPO po = InspectRecordPO.builder()
                            .taskId(taskPO.getId())
                            .taskName(taskPO.getName())
                            .planId(taskPO.getPlanId())
                            .planName(taskPO.getPlanName())
                            .targetCategory(planTargetReferVO.getTargetCategory())
                            .targetId(planTargetReferVO.getTargetId())
                            .targetName(planTargetReferVO.getTargetName())
                            .status(InspectRecordStatusEnum.EXECUTING)
                            .build();

                    this.save(po);
                    recordStatusChangeService.changeForRecordGenerate(po);

                });

    }

    @Override
    public void stopByTaskId(Long taskId) {

        List<InspectRecordPO> pos = this.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, taskId)
                .isNull(InspectRecordPO::getTime) // 只对未巡检的记录停止
                .list();
        pos.forEach(recordStatusChangeService::changeForRecordStop);
    }

    @Override
    public void submitRecord(Long id, List<InspectRecordItemCommandModel> commandModels) {
        Assert.isNotAcceptable(inspectRecordQueryService.recordIsExpired(id), "巡检任务已过期！");
        // 保存 记录项
        int abnormalCount = recordItemCommandService.saveByRecord(id, commandModels);
        // 更新 记录
        InspectRecordPO po = this.getById(id);
        this.builderRecordPO(po, null);
        po.setResult(abnormalCount > 0 ? InspectRecordResultEnum.ABNORMAL : InspectRecordResultEnum.NORMAL);
        this.updateById(po);
        recordStatusChangeService.changeForRecordComplete(po);
        sendObjInspectResultMessage(po);
        //更新任务的开始和结束时间
        this.updateTaskBeginEndTime(po);
        this.fileInduce(id, commandModels);
    }

    /**
     * 批量提交巡检的任务记录
     *
     * @param commandList 入参
     */
    public void submitInspectTaskRecordBatch(List<InspectTaskRecordSubmitCommand> commandList) {
        if (CollectionUtils.isEmpty(commandList)) {
            return;
        }
        List<Long> recordIds = new ArrayList<>();
        commandList.forEach(command -> recordIds.add(command.getRecordId()));
        Assert.isParameterError(CollectionUtils.isEmpty(recordIds), "记录不能为空");
        List<InspectRecordAndTaskDTO> recordAndTaskIdList = this.baseMapper.getRecordAndTaskId(recordIds);
        Assert.isConflict(CollectionUtils.isEmpty(recordAndTaskIdList), "记录或任务不存在");
        StringBuffer stringBuffer = new StringBuffer();
        recordAndTaskIdList.forEach(record -> {
            if (!InspectRecordStatusEnum.EXECUTING.equals(record.getStatus())) {
                stringBuffer.append(record.getTargetName()).append("已被巡检！");
            }
        });
        Assert.isConflict(!stringBuffer.isEmpty(), stringBuffer.toString());
        Map<Long, InspectRecordAndTaskDTO> recordMap = new HashMap<>();
        recordAndTaskIdList.forEach(record -> recordMap.put(record.getId(), record));

        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO userDTO = this.getUserDTO(userId);
        // 需要保存的巡检记录项集合
        List<InspectRecordItemPO> itemPOList = new ArrayList<>();
        // 巡检事件消息集合
        List<SubmitInspectResultMessage> resultMessageList = new ArrayList<>();
        // 文件持久化集合
        List<FileRefCreateParam> fileInduceParamList = new ArrayList<>();
        // 待修复的记录id集合
        List<Long> recordRepairList = new ArrayList<>();
        // 已完成的记录id集合
        List<Long> recordOverList = new ArrayList<>();
        // 任务id集合
        Set<Long> taskIdSet = new HashSet<>();
        // 设备对象记录抓拍映射 key: recordId, value: 设备id
        Map<Long, Long> snapRecordMap = new HashMap<>();
        commandList.forEach(command -> {
            Long recordId = command.getRecordId();
            // 校验
            InspectRecordAndTaskDTO temp = recordMap.get(recordId);
            Assert.isConflict(temp == null, "记录或任务不存在，请检查是否被删除！");
            if (InspectTargetCategoryEnum.SITE_POINT.name().equalsIgnoreCase(temp.getTargetCategory())
                && temp.getTargetId() != null) {
                snapRecordMap.put(recordId, temp.getTargetId());
            }
            taskIdSet.add(temp.getTaskId());
            // 巡检消息对象
            SubmitInspectResultMessage resultMessage = SubmitInspectResultMessage.builder()
                    .objId(temp.getTargetId())
                    .objType(temp.getTargetCategory())
                    .taskName(temp.getTaskName())
                    .recordId(temp.getId())
                    .inspectTime(now)
                    .userId(userId)
                    .build();
            resultMessageList.add(resultMessage);

            /* 巡检记录状态、巡检记录项、异常项个数、巡检消息体、持久化的文件*/
            AtomicInteger abnormalCount = new AtomicInteger(0);
            List<InspectRecordItemCommandModel> commandModels = command.getCommandModels();
            List<SubmitInspectResultMessage.ItemInfo> itemInfos = CollectionUtils.emptyList();
            commandModels.forEach(commandModel -> {
                // 巡检记录项
                InspectRecordItemPO recordItemPO = ObjectUtils.copying(commandModel, InspectRecordItemPO.class);
                recordItemPO.setId(IdWorker.getId());
                recordItemPO.setRecordId(recordId);
                itemPOList.add(recordItemPO);
                // 异常项个数
                if (commandModel.getErrorValueFlag()) {
                    abnormalCount.getAndIncrement();
                }
                // 处理巡检消息体
                itemInfos.add(SubmitInspectResultMessage.ItemInfo.builder()
                                      .standardName(recordItemPO.getStandardName())
                                      .itemId(recordItemPO.getItemId())
                                      .itemName(recordItemPO.getItemName())
                                      .resultName(recordItemPO.getItemResultName())
                                      .build());
                resultMessage.setItemInfos(itemInfos);

                // 处理文件
                if (!CollectionUtils.isEmpty(commandModel.getFileIds())) {
                    FileRefCreateParam param = new FileRefCreateParam();
                    param.setBusinessKey(recordItemPO.getId());
                    param.setSceneCode(MaintainFileSceneCodeEnum.INSPECTION_RECORD.getCode());
                    param.setFileIdList(commandModel.getFileIds());
                    FileRefCreateParam recordParam = new FileRefCreateParam();
                    recordParam.setBusinessKey(recordId);
                    recordParam.setSceneCode(MaintainFileSceneCodeEnum.INSPECTION_RECORD.getCode());
                    recordParam.setFileIdList(commandModel.getFileIds());
                    fileInduceParamList.add(param);
                }
            });
            // 记录状态
            if (abnormalCount.get() > 0) {
                recordRepairList.add(command.getRecordId());
            } else {
                recordOverList.add(command.getRecordId());
            }
        });
        // 更新记录状态
        InspectRecordPO updateParam = new InspectRecordPO();
        // 处理巡检人数据
        this.builderRecordPO(updateParam, userDTO);
        if (CollectionUtils.isNotEmpty(recordOverList)) {
            updateParam.setStatus(InspectRecordStatusEnum.OVER);
            updateParam.setResult(InspectRecordResultEnum.NORMAL);
            updateParam.setTime(now);
            this.baseMapper.updateStatusByIds(recordOverList, updateParam, now, userDTO.getUsername());
            updateParam.setStatus(null);
        }
        if (CollectionUtils.isNotEmpty(recordRepairList)) {
            updateParam.setStatus(InspectRecordStatusEnum.WAIT_REPAIR);
            updateParam.setRepairStatus(InspectRecordStatusEnum.WAIT_REPAIR.name());
            updateParam.setResult(InspectRecordResultEnum.ABNORMAL);
            updateParam.setTime(now);
            this.baseMapper.updateStatusByIds(recordRepairList, updateParam, now, userDTO.getUsername());
            updateParam.setStatus(null);
            updateParam.setRepairStatus(null);
            updateParam.setResult(null);
            // 插入修复对象修复人信息（方便以后修复对象做数据权限）
            this.repairRelationMapper.insertByRecordIds(recordRepairList);
        }
        // 检查和更新任务状态
        List<InspectRecordPO> recordPOList = this.lambdaQuery().in(InspectRecordPO::getTaskId, taskIdSet).list();
        handlerService.inspectTaskStatusCheck(new ArrayList<>(taskIdSet), recordPOList, SecurityContextHolder.getPrincipal());
        if (CollectionUtils.isNotEmpty(recordRepairList)) {
            handlerService.handlerRepairTodo(this.listByIds(recordRepairList), SecurityContextHolder.getPrincipal(), now);
        }
        // 保存记录项
        inspectRecordItemCommandService.saveBatch(itemPOList);
        // 文件持久化
        if (CollectionUtils.isNotEmpty(fileInduceParamList)) {
            fileRefClient.refInduceBatch(fileInduceParamList);
        }

        // 异步处理(发送对象巡检结果消息,发送巡检异常消息,发送设备生命事件)
        Principal principal = SecurityContextHolder.getPrincipal();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String BusinessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        ApplicationDTO application = applicationClient.getApplicationByCode(MaintainApplicationEnum.INSPECTION.name());
        CompletableFuture.runAsync(() -> {
            log.warn("巡检提交开始异步处理!");
            try {
                BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(BusinessType);
                SecurityContextHolder.setPrincipal(principal);
                SessionContextHolder.requestTenant(tenant);
                Map<Long, List<InspectRecordPO>> taskIdRecordMap = recordPOList.stream()
                        .collect(Collectors.groupingBy(InspectRecordPO::getTaskId));
                List<DeviceLifeEventMessage> deviceLifeMessageList = CollectionUtils.emptyList();
                taskIdRecordMap.forEach((taskId, list) -> {
                    list.forEach(record -> {
                        // 如果是设备就组装设备生命事件消息
                        if (InspectTargetCategoryEnum.DEVICE.name().equals(record.getTargetCategory())) {
                            DeviceLifeEventMessage message = new DeviceLifeEventMessage();
                            message.setTenant(record.getTenant());
                            message.setDeviceIds(Collections.singleton(record.getTargetId()));
                            message.setApplicationCode(application.getCode());
                            message.setApplicationName(application.getName());
                            message.setEventId(record.getId());
                            message.setEventStatus(record.getStatus().getDescription());
                            message.setEventName(record.getTaskName());
                            message.setEventTime(LocalDateTime.now());
                            message.setEventType(MaintainApplicationEnum.INSPECTION.name());
                            message.setEventTypeName(MaintainApplicationEnum.INSPECTION.getDescription());
                            deviceLifeMessageList.add(message);
                        }
                    });
                });
                log.warn("巡检提交异步处理,设备生命事件处理成功!");
                // 现场点关联设备抓拍，成功后将视频转存记录 id 写入巡检记录 snap_attachment_ids
                this.submitRecordSnapCapture(snapRecordMap);

                // 发送结果异常通知
                if (CollectionUtils.isNotEmpty(recordRepairList)) {
                    this.taskRecordErrorSendMessage(recordRepairList, userDTO.getName());
                }
                // 发送对象巡检消息
                if (CollectionUtils.isNotEmpty(resultMessageList)) {
                    resultMessageList.forEach(resultMessage -> RocketMQContextHolder.send(SubmitInspectResultMessage.TOPIC, resultMessage));
                }
                // 发送设备生命事件
                if (CollectionUtils.isNotEmpty(deviceLifeMessageList)) {
                    deviceLifeMessageList.forEach(deviceLifeMessage -> RocketMQContextHolder.send(DeviceLifeEventMessage.TOPIC, deviceLifeMessage));
                }
                log.warn("巡检提交结束异步处理!");
            } catch (Exception e) {
                log.error("巡检提交异步处理出错:{}", e.getMessage(), e);
            }
        });
    }

    private void submitRecordSnapCapture(Map<Long, Long> snapRecordMap) {
        if (MapUtils.isEmpty(snapRecordMap)) {
            return;
        }
        List<Long> scenePointIds = snapRecordMap.values().stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(scenePointIds)) {
            return;
        }
        Map<Long, List<Long>> scenePointToDeviceIds = inspectScenePointDeviceService.mapDeviceIdsByScenePointIds(scenePointIds);
        if (MapUtils.isEmpty(scenePointToDeviceIds)) {
            return;
        }
        Set<Long> videoDeviceIds = this.queryVideoDeviceIds();
        snapRecordMap.forEach((recordId, scenePointId) -> {
            if (scenePointId == null) {
                return;
            }
            List<Long> associatedDeviceIds = scenePointToDeviceIds.get(scenePointId);
            if (CollectionUtils.isEmpty(associatedDeviceIds)) {
                return;
            }
            LinkedHashSet<Long> cameraIds = new LinkedHashSet<>();
            for (Long deviceId : associatedDeviceIds) {
                if (deviceId == null) {
                    continue;
                }
                Long cameraId = resolveSnapCameraId(deviceId, videoDeviceIds);
                if (cameraId != null) {
                    cameraIds.add(cameraId);
                }
            }
            if (CollectionUtils.isEmpty(cameraIds)) {
                return;
            }
            List<Long> snapIds = new ArrayList<>();
            for (Long cameraId : cameraIds) {
                try {
                    VideoSnapCaptureCommand command = new VideoSnapCaptureCommand();
                    command.setLocallyStored(Boolean.TRUE);
                    command.setSceneCode(MaintainFileSceneCodeEnum.INSPECTION_RECORD.getCode());
                    VideoSnapCaptureDTO dto = videoClient.videoSnapCapture(cameraId.toString(), command);
                    if (dto != null && dto.getId() != null) {
                        snapIds.add(dto.getId());
                    }
                } catch (Exception e) {
                    log.warn("巡检现场点关联设备抓拍失败, recordId={}, scenePointId={}", recordId, scenePointId, e);
                }
            }
            if (CollectionUtils.isNotEmpty(snapIds)) {
                snapIds = snapIds.size() > 3 ? snapIds.subList(0, 3) : snapIds;
                String joined = snapIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                this.lambdaUpdate()
                        .eq(InspectRecordPO::getId, recordId)
                        .set(InspectRecordPO::getSnapAttachmentIds, joined)
                        .update();
            }

        });
    }

    private Set<Long> queryVideoDeviceIds() {
        try {
            return new HashSet<>(Optional.ofNullable(deviceCenterStdClient.getListByTag(Collections.singleton(Const.VIDEO_DEVICE_TAG_CODE)))
                                         .orElse(Collections.emptyList())
                                         .stream()
                                         .map(DeviceBaseArchivesInfoDTO::getId)
                                         .filter(Objects::nonNull)
                                         .collect(Collectors.toSet()));
        } catch (Exception e) {
            log.debug("查询摄像机设备列表失败", e);
            return Collections.emptySet();
        }
    }

    private Long resolveSnapCameraId(Long targetDeviceId, Set<Long> videoDeviceIds) {
        if (targetDeviceId == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(videoDeviceIds) && videoDeviceIds.contains(targetDeviceId)) {
            return targetDeviceId;
        }
        try {
            return deviceCenterAssociatedStdClient.getAssociatedVideos(targetDeviceId)
                    .stream()
                    .map(DeviceBaseArchivesInfoDTO::getId)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.debug("获取设备关联摄像机失败, targetDeviceId={}", targetDeviceId, e);
            return null;
        }
    }

    @Override
    public void submitTaskRecordBatch(List<InspectTaskRecordSubmitCommand> commandList) {
        String headerBusinessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        boolean businessIsSmallBool = InspectionMappingEnum.SMALL_ENGINEERING.name().equals(headerBusinessType);
        if (businessIsSmallBool) {
            this.submitSmallTaskRecordBatch(commandList);
        } else {
            this.submitInspectTaskRecordBatch(commandList);
        }
    }

    /**
     * 批量提交小散任务的巡检记录
     *
     * @param commandList 入参
     */
    public void submitSmallTaskRecordBatch(List<InspectTaskRecordSubmitCommand> commandList) {
        if (CollectionUtils.isEmpty(commandList)) {
            return;
        }
        List<Long> recordIds = new ArrayList<>();
        // 校验
        commandList.forEach(command -> {
            Assert.isNotAcceptable(inspectRecordQueryService.recordIsExpired(command.getRecordId()), "巡检任务已过期！");
            recordIds.add(command.getRecordId());
        });

        UserDTO userDTO = this.getUserDTO(SecurityContextHolder.getPrincipal().getId());
        Map<Long, InspectRecordPO> recordMap = new HashMap<>();
        List<InspectRecordPO> recordPOList = this.listByIds(recordIds);
        Optional.ofNullable(recordPOList).orElse(Collections.emptyList()).forEach(recordPO -> {recordMap.put(recordPO.getId(), recordPO);});
        // 更新状态
        commandList.forEach(command -> {
            // 更新 记录
            InspectRecordPO po = recordMap.get(command.getRecordId());
            this.builderRecordPO(po, userDTO);
            po.setResult(
                    this.getErrCount(command.getCommandModels()) > 0 ? InspectRecordResultEnum.ABNORMAL : InspectRecordResultEnum.NORMAL);
            recordStatusChangeService.changeForRecordComplete(po);
        });
        // 处理
        this.taskRecordSubmitOtherHandler(commandList);
    }

    /**
     * 文件持久化
     *
     * @param commandModels 提交(文件)
     */
    private void fileInduce(Long id, List<InspectRecordItemCommandModel> commandModels) {
        /* 化文件持久标记 */
        Set<Long> fileIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(commandModels)) {
            commandModels.forEach(c -> {
                if (!CollectionUtils.isEmpty(c.getFileIds())) {
                    fileIdSet.addAll(c.getFileIds());
                }
            });
        }
        if (!CollectionUtils.isEmpty(fileIdSet)) {
            FileInduceParam param = new FileInduceParam();
            param.setBusinessKey(id);
            param.setSceneCode(MaintainFileSceneCodeEnum.INSPECTION_RECORD.getCode());
            param.setFileIdList(new ArrayList<>(fileIdSet));
            fileOperateClient.fileInduceSingleBody(param);
        }
    }

    /**
     * @author wql
     * <p>
     * 发送对象巡检结果消息
     * <p>
     * 16:10 2023/9/18
     **/
    private void sendObjInspectResultMessage(InspectRecordPO po) {
        try {
            SubmitInspectResultMessage resultMessage = SubmitInspectResultMessage.builder()
                    .objId(po.getTargetId())
                    .objType(po.getTargetCategory())
                    .taskName(po.getTaskName())
                    .recordId(po.getId())
                    .inspectTime(po.getUpdateTime())
                    .userId(po.getUserId())
                    .build();
            List<InspectRecordItemPO> list = recordItemCommandService.list(new LambdaQueryWrapper<InspectRecordItemPO>().eq(InspectRecordItemPO::getRecordId, po.getId()));
            if (ObjectUtils.isNotEmpty(list)) {
                List<SubmitInspectResultMessage.ItemInfo> itemInfos = new ArrayList<>();
                list.forEach(itemPo -> {
                    itemInfos.add(SubmitInspectResultMessage.ItemInfo.builder()
                                          .standardName(itemPo.getStandardName())
                                          .itemId(itemPo.getItemId())
                                          .itemName(itemPo.getItemName())
                                          .resultName(itemPo.getItemResultName())
                                          .build());
                });
                resultMessage.setItemInfos(itemInfos);
            }
            RocketMQContextHolder.send(SubmitInspectResultMessage.TOPIC, resultMessage);
        } catch (Exception e) {
            log.error("发送对象巡检结果消息异常:", e);
        }
    }

    /**
     * @Author wql
     * @Description 提交临时巡检
     * @Date 10:36 2023/5/26
     **/
    @Override
    public void submitRecord(String targetCategory, Long targetId, String targetName, List<InspectRecordItemCommandModel> commandModels,
                             String areaId, String areaName
                            ) {
        // 保存 记录
        InspectRecordPO po = InspectRecordPO.builder()
                .targetCategory(targetCategory)
                .targetId(targetId)
                .areaId(areaId)
                .areaName(areaName)
                .targetName(targetName)
                .build();
        this.builderRecordPO(po, null);
        this.save(po);

        //更新 记录结果
        int abnormalCount = recordItemCommandService.saveByRecord(po.getId(), commandModels);
        po.setResult(abnormalCount > 0 ? InspectRecordResultEnum.ABNORMAL : InspectRecordResultEnum.NORMAL);
        this.updateById(po);
        recordStatusChangeService.changeForRecordComplete(po);
        this.fileInduce(po.getId(), commandModels);
    }

    @Override
    public void toBusiness(Long id, InspectRecordToBusinessCommandModel commandModel) {
        InspectRecordToBusinessPO recordToBusinessPO = ObjectUtils.copying(commandModel, InspectRecordToBusinessPO.class);
        recordToBusinessPO.setRecordId(id);
        recordToBusinessPO.setCreateTime(LocalDateTime.now());
        recordToBusinessMapper.insert(recordToBusinessPO);
    }

    @Override
    public void expireByTaskId(Long taskId) {
        List<InspectRecordPO> pos = this.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, taskId)
                .eq(InspectRecordPO::getStatus, InspectRecordStatusEnum.EXECUTING)
                .list();

        pos.forEach(po -> {
                        SessionContextHolder.requestTenant(po.getTenant());
                        recordStatusChangeService.changeForRecordExpire(po);
                    }
                   );
    }

    /**
     * 临时巡检批量提交
     *
     * @param commandList 入参
     *
     * @return 是否成功
     */
    @Override
    public Boolean submitTempRecordBatch(List<InspectRecordSubmitCommand> commandList) {
        if (CollectionUtils.isEmpty(commandList)) {
            return true;
        }
        List<InspectRecordPO> recordInsertList = new ArrayList<>();
        UserDTO userDTO = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        List<InspectRecordSubmitDTO> dtoList = new ArrayList<>();
        // 待修复的记录id集合
        List<InspectRepairRelationPO> recordRepairList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        commandList.forEach(command -> {
            boolean errorFlg = command.getCommandModels().stream().filter(c -> c.getErrorValueFlag() != null)
                    .anyMatch(InspectRecordItemCommandModel::getErrorValueFlag);
            InspectRecordPO po = InspectRecordPO.builder()
                    .id(IdWorker.getId())
                    .targetCategory(command.getTargetCategory())
                    .targetId(command.getTargetId())
                    .areaId(command.getAreaId())
                    .areaName(command.getAreaName())
                    .time(now)
                    .targetName(command.getTargetName())
                    .result(errorFlg ? InspectRecordResultEnum.ABNORMAL : InspectRecordResultEnum.NORMAL)
                    .status(errorFlg ? InspectRecordStatusEnum.WAIT_REPAIR : InspectRecordStatusEnum.OVER)
                    .build();
            this.builderRecordPO(po, userDTO);
            recordInsertList.add(po);

            if (errorFlg) {
                InspectRepairRelationPO repairRelationPO = InspectRepairRelationPO.builder()
                        .organizationId(po.getOrganizationId())
                        .organizationName(po.getOrganizationName())
                        .positionId(po.getPositionId())
                        .positionName(po.getPositionName())
                        .userId(userDTO.getId())
                        .username(userDTO.getUsername())
                        .nickname(po.getNickname())
                        .id(po.getId())
                        .createBy(userDTO.getUsername())
                        .createTime(now)
                        .creatorId(String.valueOf(userDTO.getId()))
                        .creatorType(ActorTypeEnum.USER)
                        .updateBy(userDTO.getUsername())
                        .updateTime(now)
                        .updaterId(String.valueOf(userDTO.getId()))
                        .updaterType(ActorTypeEnum.USER)
                        .tenant(SessionContextHolder.getRequester().getTenant())
                        .deleted(false)
                        .build();
                recordRepairList.add(repairRelationPO);
                po.setRepairStatus(InspectRepairStatusEnum.WAIT_REPAIR.name());
            }

            InspectRecordSubmitDTO dto = new InspectRecordSubmitDTO();
            dto.setPo(po);
            dto.setCommandModels(command.getCommandModels());
            dtoList.add(dto);
        });
        if (CollectionUtils.isNotEmpty(recordInsertList)) {
            // 先保存记录
            this.saveBatch(recordInsertList);
            // 更新其他
            this.tempRecordSubmitOtherHandler(dtoList);
        }
        if (CollectionUtils.isNotEmpty(recordRepairList)) {
            // 插入修复对象修复人信息（方便以后修复对象做数据权限）
            this.repairRelationMapper.insertBatch(recordRepairList);
        }

        return true;
    }

    /**
     * 任务记录异常通知
     *
     * @param recordIds 巡检记录ids
     */
    @Override
    public void taskRecordErrorSendMessage(List<Long> recordIds, String userName) {
        List<String> sceneList = Collections.singletonList(InspectNoticeModuleTypeEnum.TASK_RECORD_ERROR.name());
        String businessTypes = Arrays.stream(InspectionMappingEnum.values()).map(InspectionMappingEnum::name)
                .collect(Collectors.joining(","));
        // 获取任务失效通知配置
        List<MessageSendConfig> taskRecordErrorConfig = assemblyNoticeClient.getSendMsgConfig(MessageSendSceneQueryPram.builder()
                                                                                                      .businessType(businessTypes)
                                                                                                      .sceneCodes(sceneList)
                                                                                                      .build());

        log.info("{}任务记录异常通知：{}", SessionContextHolder.getRequester().getTenant(), taskRecordErrorConfig);
        taskRecordErrorConfig.forEach(config -> {
            InspectionMappingEnum businessTypeEnum = InspectionMappingEnum.getInspectionType(config.getBusinessType());
            List<InspectRecordPO> recordPOList = this.listByIds(recordIds);
            if (CollectionUtils.isEmpty(recordPOList)) {
                return;
            }
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
            // 处理通知人map
            Map<String, Set<Long>> noticeMap = this.handlerNoticeMap(config.getNoticeObjList());
            // 处理记录和任务类型关联关系
            List<RecordTaskDTO> recordTaskDTOS = this.baseMapper.getRecordIdAndTaskTypeId(recordIds);
            if (CollectionUtils.isEmpty(recordTaskDTOS)) {
                return;
            }
            Map<Long, List<RecordTaskDTO>> keyValueMap = recordTaskDTOS.stream().collect(Collectors.groupingBy(RecordTaskDTO::getTypeId));
            keyValueMap.forEach((typeId, list) -> {

                String targetNames = list.stream().map(RecordTaskDTO::getTargetName).collect(Collectors.joining(","));
                Long taskId = list.get(0).getTaskId();
                Map<String, String> paramMap = new HashMap<>();
                // 人员名称
                paramMap.put("executor", userName);
                // 对象名称
                paramMap.put("targetName", targetNames);
                Set<Long> longs = noticeMap.get(String.valueOf(typeId));
                if (CollectionUtils.isNotEmpty(longs)) {
                    MessageSendResponse response = assemblyNoticeClient.sendMsg(MessageSendParam.builder()
                                                                                        .businessId(String.valueOf(taskId))
                                                                                        .notifyId(config.getNotifyId())
                                                                                        .msgSceneCode(businessTypeEnum.getMsgSceneCode(config.getSceneCode()))
                                                                                        .notifyUsers(longs)
                                                                                        .tempParams(paramMap)
                                                                                        .build());
                    log.info("任务记录异常消息发送结果:{}", JSON.toJSONString(response));
                }
            });
        });

    }

    /**
     * 工单完成回调更新记录和任务状态
     *
     * @param processInstanceId 流程实例ID
     */
    @Override
    public void orderCompleteCallback(String processInstanceId) {
        List<InspectRecordToBusinessPO> businessPOList = recordToBusinessMapper.selectList(Wrappers.<InspectRecordToBusinessPO>lambdaQuery()
                                                                                                   .select(InspectRecordToBusinessPO::getRecordId)
                                                                                                   .eq(InspectRecordToBusinessPO::getBusinessId, processInstanceId));
        List<Long> recordIds = Optional.ofNullable(businessPOList).orElse(Collections.emptyList()).stream()
                .map(InspectRecordToBusinessPO::getRecordId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(recordIds)) {
            return;
        }
        Principal principal = SecurityContextHolder.getPrincipal();
        if (principal == null) {
            principal = Principal.builder().id(1L).account("inspect").name("system").type(Principal.Type.CLIENT).build();
        }
        var now = LocalDateTime.now();
        InspectRecordPO updateParam = new InspectRecordPO();
        updateParam.setStatus(InspectRecordStatusEnum.OVER);
        updateParam.setRepairStatus(InspectRepairStatusEnum.REPAIRED.name());
        updateParam.setTime(now);
        // 更新记录状态为已完成
        this.baseMapper.updateStatusByIds(recordIds, updateParam, now, principal.getAccount());
        // 检测更新任务状态
        handlerService.inspectTaskStatusCheck(recordIds, principal);
        /* 更新修复待办 */
        List<InspectRecordPO> recordPOList = inspectRecordQueryService.listByIds(recordIds);
        if (CollectionUtils.isNotEmpty(recordPOList)) {
            Principal finalPrincipal = principal;
            recordPOList.forEach(po -> handlerService.sendChangeTodo(po.getId(), InspectTaskStatusEnum.REPAIRED, InspectTaskStatusEnum.WAIT_REPAIR.name(), new HashMap<>(), finalPrincipal.getId(), Collections.singletonList(finalPrincipal.getId())));
        }
    }

    /**
     * 取消巡检
     *
     * @param param 取消内容
     *
     * @return 是否成功
     */
    @Override
    public Boolean batchCancelInspect(InspectRecordCancelBatchCommand param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getRecordIdList()), "记录不能为空");
        Principal principal = SecurityContextHolder.getPrincipal();
        if (principal == null) {
            principal = Principal.builder().id(1L).account("inspect").name("system").type(Principal.Type.CLIENT).build();
        }
        var now = LocalDateTime.now();
        /* 持久化文件*/
        Long cancelFileKey = IdWorker.getId();
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
        /* 更新记录状态 */
        InspectRecordPO updateParam = new InspectRecordPO();
        updateParam.setCancelInspectFileKey(cancelFileKey);
        updateParam.setCancelInspectReason(param.getContent());
        updateParam.setStatus(InspectRecordStatusEnum.CANCEL_INSPECT);
        updateParam.setTime(now);
        // 更新记录状态为取消巡检
        this.baseMapper.updateStatusByIds(param.getRecordIdList(), updateParam, now, principal.getAccount());
        // 检测更新任务状态
        handlerService.inspectTaskStatusCheck(param.getRecordIdList(), principal);
        return true;
    }

    private UserDTO getUserDTO(Long userId) {
        UserDTO dto = null;
        try {
            dto = userClient.getUser(userId);
        } catch (Exception e) {
            log.error("获取用户信息异常", e);

        }
        return dto == null ? new UserDTO() : dto;
    }

    private void updateTaskBeginEndTime(InspectRecordPO recordPO) {
        List<InspectRecordPO> recordPOS = this.lambdaQuery()
                .eq(InspectRecordPO::getTaskId, recordPO.getTaskId())
                .select(InspectRecordPO::getStatus)
                .list();

        long overCount = recordPOS.stream().filter(po -> po.getStatus() == InspectRecordStatusEnum.OVER).count();

        InspectTaskPO taskPO = taskMapper.selectById(recordPO.getTaskId());
        if (ObjectUtils.isNotEmpty(taskPO)) {
            if (overCount == recordPOS.size() || taskPO.getCheckTaskId() != 0) {
                taskStatusChangeService.changeForTaskComplete(taskPO.getId());
            }
        }
    }

    private void builderRecordPO(InspectRecordPO po, UserDTO userDTO) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        if (userDTO == null) {
            userDTO = this.getUserDTO(userId);
        }
        po.setUserId(userId);
        po.setUsername(userDTO.getUsername());
        po.setNickname(userDTO.getName());
        List<UserDTO.Organization> organizations = userDTO.getOrganizations();
        UserDTO.Position position = !userDTO.getPositions().isEmpty() ? userDTO.getPositions().get(0) : null;
        if (ObjectUtils.isNotEmpty(organizations)) {
            UserDTO.Organization organization = organizations.get(organizations.size() - 1);
            po.setOrganizationId(organization.getId());
            po.setOrganizationName(organization.getName());
        }
        if (position != null) {
            po.setPositionId(position.getId());
            po.setPositionName(position.getName());
        }
    }

    private void tempRecordSubmitOtherHandler(List<InspectRecordSubmitDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        Principal principal = SecurityContextHolder.getPrincipal();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String businessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        // 创建一个新的线程
        Thread thread = new Thread(() -> {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(businessType);
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(tenant);
            dtoList.forEach(dto -> {
                InspectRecordPO po = dto.getPo();
                List<InspectRecordItemCommandModel> commandModels = dto.getCommandModels();
                //更新 记录结果
                int abnormalCount = recordItemCommandService.saveByRecord(po.getId(), commandModels);
                this.fileInduce(po.getId(), commandModels);
            });
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }, "tempRecordSubmitOtherHandler");
        // 启动线程
        thread.start();
    }

    private void taskRecordSubmitOtherHandler(List<InspectTaskRecordSubmitCommand> commandList) {
        if (CollectionUtils.isEmpty(commandList)) {
            return;
        }
        Principal principal = SecurityContextHolder.getPrincipal();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String BusinessType = BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.get();
        // 创建一个新的线程
        Thread thread = new Thread(() -> {
            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.set(BusinessType);
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(tenant);
            List<Long> noticeRecordIdList = new ArrayList<>();
            commandList.forEach(command -> {
                Long id = command.getRecordId();
                List<InspectRecordItemCommandModel> commandModels = command.getCommandModels();
                // 保存 记录项
                int abnormalCount = recordItemCommandService.saveByRecord(id, commandModels);
                // 更新 记录
                InspectRecordPO po = this.getById(id);
                this.builderRecordPO(po, null);
                po.setResult(abnormalCount > 0 ? InspectRecordResultEnum.ABNORMAL : InspectRecordResultEnum.NORMAL);
                recordStatusChangeService.changeForRecordComplete(po);
                sendObjInspectResultMessage(po);
                //更新任务的开始和结束时间
                this.updateTaskBeginEndTime(po);
                this.fileInduce(id, commandModels);
                // 如果结果是异常加入异常通知集合
                if (InspectRecordResultEnum.ABNORMAL.equals(po.getResult())) {
                    noticeRecordIdList.add(id);
                }
            });
            if (CollectionUtils.isNotEmpty(noticeRecordIdList)) {
                // 发送异常消息
                UserDTO userDTO = this.getUserDTO(SecurityContextHolder.getPrincipal().getId());
                try {
                    // 获取业务数据信息
                    this.taskRecordErrorSendMessage(noticeRecordIdList, userDTO.getName());
                } catch (Exception e) {
                    log.error("任务异常消息提醒出错：", e);
                } finally {
                    BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
                }

            }

            BusinessTypeInterceptor.BUSINESS_TYPE_CONTEXT.remove();
        }, "taskRecordSubmitOtherHandler");

        // 启动线程
        thread.start();
    }

    private Integer getErrCount(List<InspectRecordItemCommandModel> commandModels) {
        AtomicInteger abnormalCount = new AtomicInteger(0);

        Optional.ofNullable(commandModels).orElse(Collections.emptyList())
                .forEach(commandModel -> {
                    if (commandModel.getErrorValueFlag()) {
                        abnormalCount.getAndIncrement();
                    }
                });
        return abnormalCount.get();
    }

    private Map<String, Set<Long>> handlerNoticeMap(List<AssemblyNoticeObjDTO> noticeObjList) {
        Map<String, Set<Long>> noticeMap = new HashMap<>();
        if (ObjectUtils.isNotEmpty(noticeObjList)) {
            noticeObjList.forEach(notice -> {
                Set<Long> set = new HashSet<>();
                Arrays.stream(notice.getObjId().split(",")).mapToLong(Long::parseLong).forEach(set::add);
                noticeMap.put(notice.getObjType(), set);
            });
        }
        return noticeMap;
    }

}
