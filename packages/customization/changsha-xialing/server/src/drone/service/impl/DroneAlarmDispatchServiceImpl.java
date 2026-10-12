package com.guhecloud.cube.customization.xialing.server.drone.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.alarm.client.message.AlarmRecordAssignedMessage;
import com.guhecloud.cube.base.alarm.client.message.AlarmRecordOccurredMessage;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.constants.MessageSendStatusCodeConstants;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.customization.xialing.server.drone.client.XianingDroneApiClient;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneAlarmDispatchConfigMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneAlarmDispatchNotifyStateMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneAlarmDispatchRecordMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dto.DroneAlarmDispatchNotifyPayload;
import com.guhecloud.cube.customization.xialing.server.drone.dto.DroneQuickTaskResult;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchConfig;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchNotifyState;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchRecord;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneAlarmDispatchConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneAlarmDispatchRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneQuickTaskStartParam;
import com.guhecloud.cube.customization.xialing.server.drone.param.IdNameParam;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchCaptureService;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchService;
import com.guhecloud.cube.customization.xialing.server.drone.util.Gcj02CoordinateConverter;
import com.guhecloud.cube.customization.xialing.server.drone.util.Gcj02CoordinateConverter.Coordinate;
import com.guhecloud.cube.customization.xialing.server.drone.vo.DroneAlarmDispatchConfigVO;
import com.guhecloud.cube.customization.xialing.server.drone.vo.DroneAlarmDispatchRecordVO;
import com.guhecloud.cube.customization.xialing.server.drone.vo.IdNameVO;
import com.guhecloud.cube.iot.north.bridge.client.IotDeviceClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.DeviceDetailParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 无人机报警自动派飞服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DroneAlarmDispatchServiceImpl implements DroneAlarmDispatchService {
    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_FAIL = 2;
    private static final int NOTIFY_STATUS_PENDING = 0;
    private static final int NOTIFY_STATUS_SENDING = 1;
    private static final int NOTIFY_STATUS_SUCCESS = 2;
    private static final int NOTIFY_STATUS_FAIL = 3;
    private static final String MSG_SCENE_DRONE_ALARM_DISPATCH_RESULT = "DRONE_ALARM_DISPATCH_RESULT";
    private static final String MSG_CONTENT_SUCCESS = "空地联动成功";
    private static final String MSG_CONTENT_FAIL = "空地联动失败";
    private static final BigDecimal DEFAULT_FLY_HEIGHT = new BigDecimal("120");
    private static final BigDecimal DEFAULT_ARRIVAL_DISTANCE_THRESHOLD_METERS = new BigDecimal("20");
    private static final BigDecimal DEFAULT_ARRIVAL_SPEED_THRESHOLD_CMPS = new BigDecimal("5");
    private static final int DEFAULT_GIMBAL_YT_TYPE = 31;
    private static final int DEFAULT_VIDEO_DURATION_SECONDS = 30;
    private static final int MEDIA_STATUS_PENDING = 1;
    private static final int MEDIA_STATUS_SKIPPED = 4;
    private static final String MEDIA_ACTION_PHOTO = "photo";
    private static final String MEDIA_ACTION_VIDEO = "video";
    private static final TypeReference<List<Long>> LONG_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<IdNameVO>> ID_NAME_LIST_TYPE = new TypeReference<>() {};

    private final DroneAlarmDispatchConfigMapper configMapper;
    private final DroneAlarmDispatchRecordMapper recordMapper;
    private final DroneAlarmDispatchNotifyStateMapper notifyStateMapper;
    private final XianingDroneApiClient xianingDroneApiClient;
    private final IotDeviceClient iotDeviceClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final MessageSendClient messageSendClient;
    private final DroneAlarmDispatchCaptureService captureService;

    /**
     * 查询当前租户唯一配置，未配置时返回带默认值的空视图。
     */
    @Override
    public DroneAlarmDispatchConfigVO currentConfig() {
        return toConfigVO(getCurrentConfig(currentTenant()));
    }

    /**
     * 新增或更新当前租户唯一配置。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCurrentConfig(DroneAlarmDispatchConfigSaveParam param) {
        Integer tenant = currentTenant();
        DroneAlarmDispatchConfig entity = getCurrentConfig(tenant);
        if (entity == null) {
            entity = new DroneAlarmDispatchConfig();
            entity.setId(IdWorker.getId());
            entity.setTenant(tenant);
            entity.setDeleted(false);
        }
        entity.setEnabled(Boolean.TRUE.equals(param == null ? null : param.getEnabled()));
        entity.setAlarmTargetIds(serializeIds(param == null ? null : param.getAlarmTargets()));
        entity.setAlarmTargetNames(serializeTargetNames(param == null ? null : param.getAlarmTargets()));
        entity.setAlarmLevelIds(serializeIds(param == null ? null : param.getAlarmLevels()));
        entity.setAlarmLevelNames(emptyJsonArray());
        entity.setAlarmTypeIds(serializeIds(param == null ? null : param.getAlarmTypes()));
        entity.setAlarmTypeNames(emptyJsonArray());
        entity.setDroneDeviceId(param == null ? null : param.getDroneDeviceId());
        entity.setDroneDeviceName(param == null ? null : param.getDroneDeviceName());
        entity.setFinishAction(param == null ? null : param.getFinishAction());
        entity.setFlyHeight(param == null || param.getFlyHeight() == null ? DEFAULT_FLY_HEIGHT : param.getFlyHeight());
        entity.setSceneId(param == null ? null : param.getSceneId());
        entity.setSceneCode(param == null ? null : param.getSceneCode());
        String mediaAction = normalizeMediaAction(param == null ? null : param.getMediaAction());
        entity.setMediaAction(mediaAction);
        entity.setGimbalYtType(mediaAction == null ? null : defaultIfNull(param.getGimbalYtType(), DEFAULT_GIMBAL_YT_TYPE));
        entity.setArrivalDistanceThresholdMeters(mediaAction == null ? null
                                                                     : defaultIfNull(param.getArrivalDistanceThresholdMeters(),
                                                                                     DEFAULT_ARRIVAL_DISTANCE_THRESHOLD_METERS));
        entity.setArrivalSpeedThresholdCmps(mediaAction == null ? null
                                                                : defaultIfNull(param.getArrivalSpeedThresholdCmps(),
                                                                                DEFAULT_ARRIVAL_SPEED_THRESHOLD_CMPS));
        entity.setVideoDurationSeconds(mediaAction == null ? null
                                                           : defaultIfNull(param.getVideoDurationSeconds(), DEFAULT_VIDEO_DURATION_SECONDS));
        if (configMapper.selectById(entity.getId()) == null) {
            configMapper.insert(entity);
        } else {
            configMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 按查询条件分页返回派飞日志。
     */
    @Override
    public Pagination<DroneAlarmDispatchRecordVO> pageRecords(DroneAlarmDispatchRecordPageParam param) {
        if (param == null) {
            param = new DroneAlarmDispatchRecordPageParam();
        }
        DroneAlarmDispatchRecordPageParam queryParam = param;
        var query = Wrappers.<DroneAlarmDispatchRecord>lambdaQuery()
                .eq(DroneAlarmDispatchRecord::getTenant, currentTenant())
                .eq(queryParam.getAlarmRecordId() != null, DroneAlarmDispatchRecord::getAlarmRecordId, queryParam.getAlarmRecordId())
                .eq(queryParam.getAlarmTargetId() != null, DroneAlarmDispatchRecord::getAlarmTargetId, queryParam.getAlarmTargetId())
                .eq(queryParam.getAlarmLevelId() != null, DroneAlarmDispatchRecord::getAlarmLevelId, queryParam.getAlarmLevelId())
                .eq(queryParam.getAlarmTypeId() != null, DroneAlarmDispatchRecord::getAlarmTypeId, queryParam.getAlarmTypeId())
                .eq(queryParam.getDispatchStatus() != null, DroneAlarmDispatchRecord::getDispatchStatus, queryParam.getDispatchStatus())
                .and(StringUtils.hasText(queryParam.getKeyword()), w -> w
                        .like(DroneAlarmDispatchRecord::getAlarmTargetName, queryParam.getKeyword())
                        .or()
                        .like(DroneAlarmDispatchRecord::getTaskName, queryParam.getKeyword())
                        .or()
                        .like(DroneAlarmDispatchRecord::getAlarmCode, queryParam.getKeyword()))
                .orderByDesc(DroneAlarmDispatchRecord::getDispatchTime)
                .orderByDesc(DroneAlarmDispatchRecord::getCreateTime);
        Page<DroneAlarmDispatchRecord> page = recordMapper.selectPage(PagingUtils.convert(param), query);
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(this::toRecordVO).toList());
    }

    /**
     * 查询单条派飞日志详情。
     */
    @Override
    public DroneAlarmDispatchRecordVO recordDetail(Long id) {
        if (id == null) {
            throw new ServiceException("派飞日志ID不能为空");
        }
        DroneAlarmDispatchRecord record = recordMapper.selectOne(Wrappers.<DroneAlarmDispatchRecord>lambdaQuery()
                                                                         .eq(DroneAlarmDispatchRecord::getTenant, currentTenant())
                                                                         .eq(DroneAlarmDispatchRecord::getId, id));
        if (record == null) {
            throw new ServiceException("派飞日志不存在");
        }
        return toRecordVO(record);
    }

    /**
     * 处理报警发生消息，命中配置后启动快速任务并记录派飞日志。
     */
    @Override
    public void handleAlarmOccurred(AlarmRecordOccurredMessage message) {
        if (message == null || message.getTenant() == null) {
            return;
        }
        DroneAlarmDispatchConfig config = getCurrentConfig(message.getTenant());
        if (!matches(config, message)) {
            return;
        }
        if (hasSuccessfulRecord(message.getTenant(), message.getAlarmRecordId())) {
            return;
        }
        DroneAlarmDispatchRecord record = buildBaseRecord(message, config);
        try {
            Coordinate coordinate = parseCoordinate(message.getAlarmTargetLongitude());
            Coordinate wgs84Coordinate = Gcj02CoordinateConverter.toWgs84(coordinate.longitude(), coordinate.latitude());
            record.setLongitude(wgs84Coordinate.longitude());
            record.setLatitude(wgs84Coordinate.latitude());
            String nestSn = resolveNestSn(config.getDroneDeviceId());
            record.setNestSn(nestSn);
            String taskName = buildTaskName(message);
            record.setTaskName(taskName);
            DroneQuickTaskStartParam quickTaskParam = new DroneQuickTaskStartParam();
            quickTaskParam.setLongitude(coordinate.longitude());
            quickTaskParam.setLatitude(coordinate.latitude());
            quickTaskParam.setNestSn(nestSn);
            quickTaskParam.setTaskName(taskName);
            quickTaskParam.setSceneId(config.getSceneId());
            quickTaskParam.setSceneCode(config.getSceneCode());
            quickTaskParam.setFinishAction(config.getFinishAction());
            quickTaskParam.setFlyHeight(config.getFlyHeight());
            record.setRequestBody(JsonUtils.serialize(quickTaskParam));
            DroneQuickTaskResult result = xianingDroneApiClient.startQuickTask(quickTaskParam);
            record.setResponseBody(result.getRawResponse());
            if (Boolean.TRUE.equals(result.getSuccess())) {
                record.setDispatchStatus(STATUS_SUCCESS);
                record.setNestName(result.getNestName());
                record.setTrackFileType(result.getTrackFileType());
                record.setTrackId(result.getTrackId());
                record.setTrackUrl(result.getTrackUrl());
                initMediaFields(record, config);
                log.info("XIANINGDRONE_DISPATCH_SUCCESS alarmRecordId={} nestSn={} mediaAction={} mediaStatus={} targetLng={} targetLat={}",
                         message.getAlarmRecordId(), record.getNestSn(), record.getMediaAction(), record.getMediaStatus(),
                         record.getLongitude(), record.getLatitude());
            } else {
                record.setDispatchStatus(STATUS_FAIL);
                record.setFailReason(limit(result.getErrorMessage()));
                log.warn("XIANINGDRONE_DISPATCH_FAIL alarmRecordId={} nestSn={} reason={}", message.getAlarmRecordId(), record.getNestSn(),
                         record.getFailReason());
            }
        } catch (Exception e) {
            log.warn("XIANINGDRONE_DISPATCH_EXCEPTION alarmRecordId={}", message.getAlarmRecordId(), e);
            record.setDispatchStatus(STATUS_FAIL);
            record.setFailReason(limit(e.getMessage()));
        }
        recordMapper.insert(record);
        log.info("XIANINGDRONE_DISPATCH_RECORD_SAVED dispatchRecordId={} alarmRecordId={} dispatchStatus={} mediaAction={} mediaStatus={}",
                 record.getId(), record.getAlarmRecordId(), record.getDispatchStatus(), record.getMediaAction(), record.getMediaStatus());
        registerCaptureIfNeeded(record, config);
        trySendDispatchResultNotify(message.getTenant(), message.getAlarmRecordId());
    }

    /**
     * 处理报警初始化指派完成消息，存在派飞记录时发送空地联动结果通知。
     */
    @Override
    public void handleAlarmAssigned(AlarmRecordAssignedMessage message) {
        if (message == null || message.getTenant() == null || message.getAlarmRecordId() == null) {
            return;
        }
        saveAssignedNotifyState(message, notifyUserIds(message));
        trySendDispatchResultNotify(message.getTenant(), message.getAlarmRecordId());
    }

    /**
     * 获取当前租户唯一配置。
     */
    private DroneAlarmDispatchConfig getCurrentConfig(Integer tenant) {
        return configMapper.selectList(Wrappers.<DroneAlarmDispatchConfig>lambdaQuery()
                                               .eq(DroneAlarmDispatchConfig::getTenant, tenant)
                                               .eq(DroneAlarmDispatchConfig::getDeleted, false)
                                               .orderByAsc(DroneAlarmDispatchConfig::getCreateTime))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 判断报警消息是否同时命中对象、级别、类型和无人机配置。
     */
    private boolean matches(DroneAlarmDispatchConfig config, AlarmRecordOccurredMessage message) {
        return config != null
               && Boolean.TRUE.equals(config.getEnabled())
               && containsId(config.getAlarmTargetIds(), message.getAlarmTargetKey())
               && containsId(config.getAlarmLevelIds(), message.getAlarmLevelId())
               && containsId(config.getAlarmTypeIds(), message.getAlarmTypeId())
               && config.getDroneDeviceId() != null;
    }

    /**
     * 判断报警记录是否已存在成功派飞日志，用于避免重复成功派飞。
     */
    private boolean hasSuccessfulRecord(Integer tenant, Long alarmRecordId) {
        if (alarmRecordId == null) {
            return false;
        }
        return recordMapper.selectCount(Wrappers.<DroneAlarmDispatchRecord>lambdaQuery()
                                                .eq(DroneAlarmDispatchRecord::getTenant, tenant)
                                                .eq(DroneAlarmDispatchRecord::getAlarmRecordId, alarmRecordId)
                                                .eq(DroneAlarmDispatchRecord::getDispatchStatus, STATUS_SUCCESS)
                                                .eq(DroneAlarmDispatchRecord::getDeleted, false)) > 0;
    }

    /**
     * 查询报警记录对应的最新派飞日志，存在日志才发送联动结果通知。
     */
    private DroneAlarmDispatchRecord findDispatchRecord(Integer tenant, Long alarmRecordId) {
        return recordMapper.selectList(Wrappers.<DroneAlarmDispatchRecord>lambdaQuery()
                                               .eq(DroneAlarmDispatchRecord::getTenant, tenant)
                                               .eq(DroneAlarmDispatchRecord::getAlarmRecordId, alarmRecordId)
                                               .eq(DroneAlarmDispatchRecord::getDeleted, false)
                                               .orderByDesc(DroneAlarmDispatchRecord::getDispatchTime)
                                               .orderByDesc(DroneAlarmDispatchRecord::getCreateTime))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * 保存报警初始化指派完成状态和通知人快照。
     */
    private void saveAssignedNotifyState(AlarmRecordAssignedMessage message, Set<Long> userIds) {
        DroneAlarmDispatchNotifyState state = findNotifyState(message.getTenant(), message.getAlarmRecordId());
        if (state == null) {
            state = new DroneAlarmDispatchNotifyState();
            state.setId(IdWorker.getId());
            state.setTenant(message.getTenant());
            state.setDeleted(false);
            state.setAlarmRecordId(message.getAlarmRecordId());
            state.setAssignedReceived(true);
            state.setNotifyUserIds(JsonUtils.serialize(userIds));
            state.setNotifyStatus(NOTIFY_STATUS_PENDING);
            try {
                notifyStateMapper.insert(state);
                return;
            } catch (DuplicateKeyException e) {
                log.debug("无人机报警派飞结果通知状态已存在，改为更新 alarmRecordId={}", message.getAlarmRecordId());
            }
        }
        DroneAlarmDispatchNotifyState update = new DroneAlarmDispatchNotifyState();
        update.setAssignedReceived(true);
        update.setNotifyUserIds(JsonUtils.serialize(userIds));
        notifyStateMapper.update(update, Wrappers.<DroneAlarmDispatchNotifyState>lambdaUpdate()
                                                .eq(DroneAlarmDispatchNotifyState::getTenant, message.getTenant())
                                                .eq(DroneAlarmDispatchNotifyState::getAlarmRecordId, message.getAlarmRecordId())
                                                .eq(DroneAlarmDispatchNotifyState::getDeleted, false)
                                                .ne(DroneAlarmDispatchNotifyState::getNotifyStatus, NOTIFY_STATUS_SUCCESS));
    }

    /**
     * 查询报警记录对应的派飞结果通知状态。
     */
    private DroneAlarmDispatchNotifyState findNotifyState(Integer tenant, Long alarmRecordId) {
        return notifyStateMapper.selectOne(Wrappers.<DroneAlarmDispatchNotifyState>lambdaQuery()
                                                   .eq(DroneAlarmDispatchNotifyState::getTenant, tenant)
                                                   .eq(DroneAlarmDispatchNotifyState::getAlarmRecordId, alarmRecordId)
                                                   .eq(DroneAlarmDispatchNotifyState::getDeleted, false));
    }

    /**
     * 派飞结果和报警指派状态都就绪时，幂等发送空地联动结果通知。
     */
    private void trySendDispatchResultNotify(Integer tenant, Long alarmRecordId) {
        if (tenant == null || alarmRecordId == null) {
            return;
        }
        DroneAlarmDispatchNotifyState state = findNotifyState(tenant, alarmRecordId);
        if (state == null || !Boolean.TRUE.equals(state.getAssignedReceived())) {
            log.info("无人机报警派飞结果消息暂不发送，等待报警指派完成 alarmRecordId={}", alarmRecordId);
            return;
        }
        if (Objects.equals(state.getNotifyStatus(), NOTIFY_STATUS_SUCCESS)) {
            log.info("无人机报警派飞结果消息已发送，跳过重复通知 alarmRecordId={}", alarmRecordId);
            return;
        }
        Set<Long> userIds = parseNotifyUserIds(state.getNotifyUserIds());
        if (userIds.isEmpty()) {
            markNotifyFail(state.getId(), state.getDispatchRecordId(), "报警通知人为空");
            log.warn("无人机报警派飞结果消息未发送，报警通知人为空 alarmRecordId={}", alarmRecordId);
            return;
        }
        DroneAlarmDispatchRecord record = findDispatchRecord(tenant, alarmRecordId);
        if (record == null) {
            log.info("无人机报警派飞结果消息暂不发送，等待派飞记录生成 alarmRecordId={}", alarmRecordId);
            return;
        }
        if (!acquireNotifySending(state.getId(), record.getId())) {
            log.info("无人机报警派飞结果消息发送权未获取，跳过本次触发 alarmRecordId={} dispatchRecordId={}", alarmRecordId, record.getId());
            return;
        }
        String failReason = sendDispatchResultMessage(alarmRecordId, record, userIds);
        if (failReason == null) {
            markNotifySuccess(state.getId(), record.getId());
            log.info("无人机报警派飞结果消息发送成功 alarmRecordId={} dispatchRecordId={}", alarmRecordId, record.getId());
        } else {
            markNotifyFail(state.getId(), record.getId(), failReason);
            log.warn("无人机报警派飞结果消息发送失败 alarmRecordId={} dispatchRecordId={} reason={}", alarmRecordId, record.getId(), failReason);
        }
    }

    /**
     * 抢占通知发送权，避免两个消费者并发触发时重复发送。
     */
    private boolean acquireNotifySending(Long notifyStateId, Long dispatchRecordId) {
        return notifyStateMapper.update(null, Wrappers.<DroneAlarmDispatchNotifyState>lambdaUpdate()
                                                     .set(DroneAlarmDispatchNotifyState::getNotifyStatus, NOTIFY_STATUS_SENDING)
                                                     .set(DroneAlarmDispatchNotifyState::getDispatchRecordId, dispatchRecordId)
                                                     .set(DroneAlarmDispatchNotifyState::getNotifyFailReason, null)
                                                     .eq(DroneAlarmDispatchNotifyState::getId, notifyStateId)
                                                     .eq(DroneAlarmDispatchNotifyState::getDeleted, false)
                                                     .in(DroneAlarmDispatchNotifyState::getNotifyStatus, NOTIFY_STATUS_PENDING,
                                                         NOTIFY_STATUS_FAIL)) > 0;
    }

    /**
     * 标记通知发送成功。
     */
    private void markNotifySuccess(Long notifyStateId, Long dispatchRecordId) {
        notifyStateMapper.update(null, Wrappers.<DroneAlarmDispatchNotifyState>lambdaUpdate()
                                               .set(DroneAlarmDispatchNotifyState::getNotifyStatus, NOTIFY_STATUS_SUCCESS)
                                               .set(DroneAlarmDispatchNotifyState::getDispatchRecordId, dispatchRecordId)
                                               .set(DroneAlarmDispatchNotifyState::getNotifySendTime, LocalDateTime.now())
                                               .set(DroneAlarmDispatchNotifyState::getNotifyFailReason, null)
                                               .eq(DroneAlarmDispatchNotifyState::getId, notifyStateId)
                                               .eq(DroneAlarmDispatchNotifyState::getDeleted, false));
    }

    /**
     * 标记通知发送失败。
     */
    private void markNotifyFail(Long notifyStateId, Long dispatchRecordId, String failReason) {
        notifyStateMapper.update(null, Wrappers.<DroneAlarmDispatchNotifyState>lambdaUpdate()
                                               .set(DroneAlarmDispatchNotifyState::getNotifyStatus, NOTIFY_STATUS_FAIL)
                                               .set(dispatchRecordId != null, DroneAlarmDispatchNotifyState::getDispatchRecordId, dispatchRecordId)
                                               .set(DroneAlarmDispatchNotifyState::getNotifyFailReason, limit(failReason))
                                               .eq(DroneAlarmDispatchNotifyState::getId, notifyStateId)
                                               .eq(DroneAlarmDispatchNotifyState::getDeleted, false)
                                               .ne(DroneAlarmDispatchNotifyState::getNotifyStatus, NOTIFY_STATUS_SUCCESS));
    }

    /**
     * 向报警通知人发送空地联动结果站内消息。
     */
    private String sendDispatchResultMessage(Long alarmRecordId, DroneAlarmDispatchRecord record, Set<Long> userIds) {
        try {
            boolean success = Objects.equals(record.getDispatchStatus(), STATUS_SUCCESS);
            String content = success ? MSG_CONTENT_SUCCESS : MSG_CONTENT_FAIL;
            Map<String, String> tmpParams = new HashMap<>();
            tmpParams.put("message", content);
            tmpParams.put("alarmTargetName", safeText(record.getAlarmTargetName()));
            tmpParams.put("taskName", safeText(record.getTaskName()));
            tmpParams.put("failReason", safeText(record.getFailReason()));
            MessageNotifyTarget notifyTarget = MessageNotifyTarget.builder()
                    .notifyTargetType(MessageNotifyTargetEnum.USER)
                    .ids(userIds)
                    .build();
            MessageSceneSendRequest request = MessageSceneSendRequest.builder()
                    .msgSceneCode(MSG_SCENE_DRONE_ALARM_DISPATCH_RESULT)
                    .applicationCode("DRONE")
                    .applicationName("无人机")
                    .businessId(String.valueOf(record.getId()))
                    .businessPrincipal(buildNotifyPayload(record, success, content))
                    .tmpParams(tmpParams)
                    .messageNotifyTargets(Collections.singletonList(notifyTarget))
                    .tenant(record.getTenant())
                    .build();

            MessageSendResponse response = messageSendClient.sceneSend(request);
            if (response == null || !MessageSendStatusCodeConstants.SEND_SUCCESS.equals(response.getCode())) {
                return limit(JsonUtils.serialize(response));
            } else {
                return null;
            }
        } catch (Exception e) {
            log.warn("无人机报警派飞结果消息发送异常 alarmRecordId={} dispatchRecordId={}", alarmRecordId, record.getId(), e);
            return limit(e.getMessage());
        }
    }

    /**
     * 从报警初始化指派完成消息中提取报警通知人ID集合。
     */
    private Set<Long> notifyUserIds(AlarmRecordAssignedMessage message) {
        List<AlarmRecordAssignedMessage.UserMessage> users =
                message.getNotifyUsers() == null ? message.getHandlerUsers() : message.getNotifyUsers();
        if (users == null) {
            return Collections.emptySet();
        }
        return users.stream()
                .filter(Objects::nonNull)
                .map(AlarmRecordAssignedMessage.UserMessage::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 从通知状态中解析报警通知人ID集合。
     */
    private Set<Long> parseNotifyUserIds(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptySet();
        }
        List<Long> ids = JsonUtils.deserialize(json, LONG_LIST_TYPE);
        if (ids == null) {
            return Collections.emptySet();
        }
        return ids.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 构造消息业务主体。
     */
    private DroneAlarmDispatchNotifyPayload buildNotifyPayload(DroneAlarmDispatchRecord record, boolean success, String content) {
        DroneAlarmDispatchNotifyPayload payload = new DroneAlarmDispatchNotifyPayload();
        payload.setAlarmRecordId(record.getAlarmRecordId());
        payload.setDispatchRecordId(record.getId());
        payload.setDispatchStatus(record.getDispatchStatus());
        payload.setSuccess(success);
        payload.setMessage(content);
        payload.setFailReason(record.getFailReason());
        payload.setTaskName(record.getTaskName());
        payload.setAlarmTargetName(record.getAlarmTargetName());
        payload.setDispatchTime(record.getDispatchTime());
        return payload;
    }

    /**
     * 基于报警消息和配置构建日志基础字段。
     */
    private DroneAlarmDispatchRecord buildBaseRecord(AlarmRecordOccurredMessage message, DroneAlarmDispatchConfig config) {
        DroneAlarmDispatchRecord record = new DroneAlarmDispatchRecord();
        record.setId(IdWorker.getId());
        record.setTenant(message.getTenant());
        record.setDeleted(false);
        record.setAlarmRecordId(message.getAlarmRecordId());
        record.setAlarmCode(message.getCode());
        record.setConfigId(config.getId());
        record.setAlarmTargetId(message.getAlarmTargetKey());
        record.setAlarmTargetName(message.getAlarmTargetName());
        record.setAlarmLevelId(message.getAlarmLevelId());
        record.setAlarmLevelName(message.getAlarmLevelName());
        record.setAlarmTypeId(message.getAlarmTypeId());
        record.setAlarmTypeName(message.getAlarmTypeName());
        record.setDroneDeviceId(config.getDroneDeviceId());
        record.setDroneDeviceName(config.getDroneDeviceName());
        record.setDispatchTime(LocalDateTime.now());
        return record;
    }

    /**
     * 解析配置无人机设备对应的机库编号。
     */
    private String resolveNestSn(Long deviceId) {
        if (deviceId == null) {
            throw new ServiceException("无人机设备未配置");
        }
        String nestSn = resolveNestSnFromIotSnapshot(deviceId);
        if (StringUtils.hasText(nestSn)) {
            return nestSn;
        }
        nestSn = resolveNestSnFromThirdpartyCfg(deviceId);
        if (StringUtils.hasText(nestSn)) {
            return nestSn;
        }
        throw new ServiceException("无人机设备未配置nestSn属性");
    }

    /**
     * 从物联设备实时快照中读取nestSn属性。
     */
    private String resolveNestSnFromIotSnapshot(Long deviceId) {
        DeviceDetailParam param = new DeviceDetailParam();
        param.setDeviceId(String.valueOf(deviceId));
        param.setWithSnapShot(true);
        param.setWithPropertySpec(true);
        DeviceDetailDTO detail = iotDeviceClient.detail(param);
        if (detail == null || detail.getSnapshots() == null) {
            return null;
        }
        for (KeyValueWithTsDTO snapshot : detail.getSnapshots()) {
            if (snapshot != null && "nestSn".equals(snapshot.getKey()) && snapshot.getValue() != null) {
                return String.valueOf(snapshot.getValue());
            }
        }
        return null;
    }

    /**
     * 从设备三方配置JSON中兜底读取nestSn。
     */
    private String resolveNestSnFromThirdpartyCfg(Long deviceId) {
        try {
            List<DeviceBaseDetailsDTO> details = deviceCenterStdClient.getDetailsListByDeviceId(Collections.singleton(deviceId));
            DeviceBaseDetailsDTO detail = details == null ? null : details.stream().filter(Objects::nonNull).findFirst().orElse(null);
            if (detail == null || !StringUtils.hasText(detail.getThirdpartyCfg())) {
                return null;
            }
            JsonNode root = JsonUtils.deserialize(detail.getThirdpartyCfg());
            return text(root == null ? null : root.get("nestSn"));
        } catch (Exception e) {
            log.debug("从设备三方配置读取nestSn失败 deviceId={}", deviceId, e);
            return null;
        }
    }

    /**
     * 解析报警对象经纬度，支持JSON和经度纬度逗号分隔格式。
     */
    private Coordinate parseCoordinate(String raw) {
        if (!StringUtils.hasText(raw)) {
            throw new ServiceException("报警对象经纬度为空");
        }
        String value = raw.trim();
        if (value.startsWith("{")) {
            JsonNode root = JsonUtils.deserialize(value);
            BigDecimal longitude = decimal(root.get("longitude"));
            BigDecimal latitude = decimal(root.get("latitude"));
            if (longitude == null) {
                longitude = decimal(root.get("lng"));
            }
            if (latitude == null) {
                latitude = decimal(root.get("lat"));
            }
            if (longitude != null && latitude != null) {
                return new Coordinate(longitude, latitude);
            }
        }
        String[] parts = value.split(",");
        if (parts.length == 2) {
            return new Coordinate(new BigDecimal(parts[0].trim()), new BigDecimal(parts[1].trim()));
        }
        throw new ServiceException("报警对象经纬度格式不正确");
    }

    /**
     * 将配置实体转换为前端回显对象。
     */
    private DroneAlarmDispatchConfigVO toConfigVO(DroneAlarmDispatchConfig entity) {
        DroneAlarmDispatchConfigVO vo = new DroneAlarmDispatchConfigVO();
        if (entity == null) {
            vo.setEnabled(false);
            vo.setFlyHeight(DEFAULT_FLY_HEIGHT);
            vo.setGimbalYtType(DEFAULT_GIMBAL_YT_TYPE);
            vo.setArrivalDistanceThresholdMeters(DEFAULT_ARRIVAL_DISTANCE_THRESHOLD_METERS);
            vo.setArrivalSpeedThresholdCmps(DEFAULT_ARRIVAL_SPEED_THRESHOLD_CMPS);
            vo.setVideoDurationSeconds(DEFAULT_VIDEO_DURATION_SECONDS);
            return vo;
        }
        vo.setId(entity.getId());
        vo.setEnabled(entity.getEnabled());
        vo.setAlarmTargets(parseIdNames(entity.getAlarmTargetNames()));
        vo.setAlarmLevels(parseIdsAsIdNames(entity.getAlarmLevelIds()));
        vo.setAlarmTypes(parseIdsAsIdNames(entity.getAlarmTypeIds()));
        vo.setDroneDeviceId(entity.getDroneDeviceId());
        vo.setDroneDeviceName(entity.getDroneDeviceName());
        vo.setFinishAction(entity.getFinishAction());
        vo.setFlyHeight(entity.getFlyHeight());
        vo.setSceneId(entity.getSceneId());
        vo.setSceneCode(entity.getSceneCode());
        vo.setMediaAction(entity.getMediaAction());
        vo.setGimbalYtType(entity.getGimbalYtType() == null ? DEFAULT_GIMBAL_YT_TYPE : entity.getGimbalYtType());
        vo.setArrivalDistanceThresholdMeters(entity.getArrivalDistanceThresholdMeters() == null ? DEFAULT_ARRIVAL_DISTANCE_THRESHOLD_METERS
                                                                                                : entity.getArrivalDistanceThresholdMeters());
        vo.setArrivalSpeedThresholdCmps(entity.getArrivalSpeedThresholdCmps() == null ? DEFAULT_ARRIVAL_SPEED_THRESHOLD_CMPS
                                                                                      : entity.getArrivalSpeedThresholdCmps());
        vo.setVideoDurationSeconds(entity.getVideoDurationSeconds() == null ? DEFAULT_VIDEO_DURATION_SECONDS : entity.getVideoDurationSeconds());
        return vo;
    }

    /**
     * 将派飞日志实体转换为展示对象。
     */
    private DroneAlarmDispatchRecordVO toRecordVO(DroneAlarmDispatchRecord entity) {
        DroneAlarmDispatchRecordVO vo = new DroneAlarmDispatchRecordVO();
        vo.setId(entity.getId());
        vo.setAlarmRecordId(entity.getAlarmRecordId());
        vo.setAlarmCode(entity.getAlarmCode());
        vo.setAlarmTargetId(entity.getAlarmTargetId());
        vo.setAlarmTargetName(entity.getAlarmTargetName());
        vo.setAlarmLevelId(entity.getAlarmLevelId());
        vo.setAlarmLevelName(entity.getAlarmLevelName());
        vo.setAlarmTypeId(entity.getAlarmTypeId());
        vo.setAlarmTypeName(entity.getAlarmTypeName());
        vo.setDroneDeviceId(entity.getDroneDeviceId());
        vo.setDroneDeviceName(entity.getDroneDeviceName());
        vo.setNestSn(entity.getNestSn());
        vo.setLongitude(entity.getLongitude());
        vo.setLatitude(entity.getLatitude());
        vo.setTaskName(entity.getTaskName());
        vo.setDispatchStatus(entity.getDispatchStatus());
        vo.setFailReason(entity.getFailReason());
        vo.setTrackFileType(entity.getTrackFileType());
        vo.setTrackId(entity.getTrackId());
        vo.setTrackUrl(entity.getTrackUrl());
        vo.setNestName(entity.getNestName());
        vo.setRequestBody(entity.getRequestBody());
        vo.setResponseBody(entity.getResponseBody());
        vo.setMediaAction(entity.getMediaAction());
        vo.setMediaStatus(entity.getMediaStatus());
        vo.setMediaFailReason(entity.getMediaFailReason());
        vo.setMediaRequestBody(entity.getMediaRequestBody());
        vo.setMediaResponseBody(entity.getMediaResponseBody());
        vo.setMediaTriggerTime(entity.getMediaTriggerTime());
        vo.setDispatchTime(entity.getDispatchTime());
        return vo;
    }

    /**
     * 根据配置初始化媒体触发日志字段。
     */
    private void initMediaFields(DroneAlarmDispatchRecord record, DroneAlarmDispatchConfig config) {
        String mediaAction = normalizeMediaAction(config.getMediaAction());
        record.setMediaAction(mediaAction);
        record.setMediaStatus(mediaAction == null ? MEDIA_STATUS_SKIPPED : MEDIA_STATUS_PENDING);
    }

    /**
     * 派飞成功且配置了执行动作时，注册 Redis 待拍摄任务。
     */
    private void registerCaptureIfNeeded(DroneAlarmDispatchRecord record, DroneAlarmDispatchConfig config) {
        if (!Objects.equals(record.getDispatchStatus(), STATUS_SUCCESS)) {
            log.info("XIANINGDRONE_CAPTURE_SKIP dispatchRecordId={} reason=dispatch_not_success dispatchStatus={}", record.getId(),
                     record.getDispatchStatus());
            return;
        }
        if (normalizeMediaAction(record.getMediaAction()) == null) {
            log.info("XIANINGDRONE_CAPTURE_SKIP dispatchRecordId={} reason=media_action_empty", record.getId());
            return;
        }
        try {
            log.info("XIANINGDRONE_CAPTURE_REGISTER_BEGIN dispatchRecordId={} tenant={} nestSn={} mediaAction={}", record.getId(),
                     record.getTenant(), record.getNestSn(), record.getMediaAction());
            captureService.register(record, config);
            log.info("XIANINGDRONE_CAPTURE_REGISTER_SUCCESS dispatchRecordId={} tenant={} nestSn={}", record.getId(), record.getTenant(),
                     record.getNestSn());
        } catch (Exception e) {
            log.warn("XIANINGDRONE_CAPTURE_REGISTER_FAIL dispatchRecordId={} tenant={} nestSn={}", record.getId(), record.getTenant(),
                     record.getNestSn(), e);
            DroneAlarmDispatchRecord update = new DroneAlarmDispatchRecord();
            update.setId(record.getId());
            update.setMediaStatus(3);
            update.setMediaFailReason(limit(e.getMessage()));
            recordMapper.updateById(update);
        }
    }

    /**
     * 归一化媒体动作配置。
     */
    private String normalizeMediaAction(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String action = value.trim().toLowerCase();
        if (MEDIA_ACTION_PHOTO.equals(action) || MEDIA_ACTION_VIDEO.equals(action)) {
            return action;
        }
        throw new ServiceException("执行动作必须为photo或video");
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 判断JSON数组中是否包含指定ID。
     */
    private boolean containsId(String json, Long id) {
        if (id == null || !StringUtils.hasText(json)) {
            return false;
        }
        List<Long> ids = JsonUtils.deserialize(json, LONG_LIST_TYPE);
        return ids != null && ids.contains(id);
    }

    /**
     * 将ID名称列表序列化为ID数组JSON。
     */
    private String serializeIds(List<IdNameParam> items) {
        List<Long> ids = items == null ? Collections.emptyList() : items.stream().map(IdNameParam::getId).filter(Objects::nonNull).toList();
        return JsonUtils.serialize(ids);
    }

    /**
     * 返回空JSON数组字符串，用于清空不再保存的名称快照字段。
     */
    private String emptyJsonArray() {
        return JsonUtils.serialize(Collections.emptyList());
    }

    /**
     * 将报警对象列表序列化为带设备编号和区域快照的JSON。
     */
    private String serializeTargetNames(List<IdNameParam> items) {
        List<IdNameVO> names = items == null ? Collections.emptyList()
                                             : items.stream()
                                       .filter(item -> item != null && item.getId() != null)
                                       .map(this::toTargetSnapshot)
                                       .toList();
        return JsonUtils.serialize(names);
    }

    /**
     * 构造报警对象回显快照。
     */
    private IdNameVO toTargetSnapshot(IdNameParam item) {
        IdNameVO vo = new IdNameVO(item.getId(), item.getName());
        vo.setCode(item.getCode());
        vo.setAreaId(item.getAreaId());
        vo.setAreaName(item.getAreaName());
        return vo;
    }

    /**
     * 解析回显快照JSON。
     */
    private List<IdNameVO> parseIdNames(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        List<IdNameVO> list = JsonUtils.deserialize(json, ID_NAME_LIST_TYPE);
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 将ID数组JSON解析为只包含ID的回显对象列表。
     */
    private List<IdNameVO> parseIdsAsIdNames(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        List<Long> ids = JsonUtils.deserialize(json, LONG_LIST_TYPE);
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream().filter(Objects::nonNull).map(id -> new IdNameVO(id, null)).toList();
    }

    /**
     * 根据报警对象和报警记录生成快速任务名称。
     */
    private String buildTaskName(AlarmRecordOccurredMessage message) {
        String targetName = StringUtils.hasText(message.getAlarmTargetName()) ? message.getAlarmTargetName() : "未知对象";
        return limit("报警核查-" + targetName + "-" + message.getAlarmRecordId(), 255);
    }

    /**
     * 获取当前请求租户。
     */
    private Integer currentTenant() {
        return SessionContextHolder.getRequester().getTenant();
    }

    /**
     * 将JSON节点转换为高精度数字。
     */
    private BigDecimal decimal(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        return new BigDecimal(node.asText());
    }

    /**
     * 将JSON节点转换为非空字符串。
     */
    private String text(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? text : null;
    }

    /**
     * 模板参数不传null，避免消息模板渲染出现空指针或字面量null。
     */
    private String safeText(String value) {
        return value == null ? "" : value;
    }

    /**
     * 将文本限制在默认长度内。
     */
    private String limit(String value) {
        return limit(value, 2000);
    }

    /**
     * 将文本限制在指定长度内。
     */
    private String limit(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max);
    }
}
