package com.guhecloud.cube.customization.xialing.server.drone.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.customization.xialing.server.drone.client.XianingDroneApiClient;
import com.guhecloud.cube.customization.xialing.server.drone.dao.DroneAlarmDispatchRecordMapper;
import com.guhecloud.cube.customization.xialing.server.drone.dto.DronePendingCaptureTask;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchConfig;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchRecord;
import com.guhecloud.cube.customization.xialing.server.drone.mqtt.DroneAlarmDispatchMqttClientManager;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchCaptureService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 报警派飞到点拍摄任务服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DroneAlarmDispatchCaptureServiceImpl implements DroneAlarmDispatchCaptureService {
    private static final String PENDING_KEY_PREFIX = "drone:alarm-dispatch:capture:pending:";
    private static final String NEST_TASK_KEY_PREFIX = "drone:alarm-dispatch:capture:nest:";
    private static final String LOCK_KEY_PREFIX = "drone:alarm-dispatch:capture:lock:";
    private static final long PENDING_TTL_MINUTES = 30L;
    private static final long LOCK_TTL_SECONDS = 60L;
    private static final int MEDIA_STATUS_SUCCESS = 2;
    private static final int MEDIA_STATUS_FAIL = 3;
    private static final String MEDIA_ACTION_PHOTO = "photo";
    private static final String MEDIA_ACTION_VIDEO = "video";
    private static final String GIMBAL_TYPE_PHOTOGRAPH = "photograph";
    private static final String GIMBAL_TYPE_VIDEO_START = "video_start";
    private static final String GIMBAL_TYPE_VIDEO_END = "video_end";
    private static final int CATEGORY_START = 1;
    private static final int CATEGORY_END = 0;
    private static final int DEFAULT_GIMBAL_YT_TYPE = 31;
    private static final int DEFAULT_VIDEO_DURATION_SECONDS = 30;
    private static final BigDecimal DEFAULT_ARRIVAL_DISTANCE_THRESHOLD_METERS = new BigDecimal("20");
    private static final BigDecimal DEFAULT_ARRIVAL_SPEED_THRESHOLD_CMPS = new BigDecimal("5");
    private static final int MAX_REASON_LENGTH = 1000;
    private static final double EARTH_RADIUS_METERS = 6371008.8D;

    private final StringRedisTemplate stringRedisTemplate;
    private final DroneAlarmDispatchRecordMapper recordMapper;
    private final XianingDroneApiClient xianingDroneApiClient;
    private final DroneAlarmDispatchMqttClientManager mqttClientManager;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new CaptureThreadFactory());

    /**
     * 派飞成功后创建待拍摄任务：pending key 保存任务详情，nest set 用于 MQTT 按机库快速找到任务。
     */
    @Override
    public void register(DroneAlarmDispatchRecord record, DroneAlarmDispatchConfig config) {
        DronePendingCaptureTask task = buildTask(record, config);
        String pendingKey = pendingKey(task.getDispatchRecordId());
        String nestKey = nestKey(task.getTenant(), task.getNestSn());
        log.info("XIANINGDRONE_CAPTURE_REDIS_SAVE_BEGIN dispatchRecordId={} tenant={} nestSn={} mediaAction={} pendingKey={} nestKey={}",
                 task.getDispatchRecordId(), task.getTenant(), task.getNestSn(), task.getMediaAction(), pendingKey, nestKey);
        stringRedisTemplate.opsForValue().set(pendingKey, JsonUtils.serialize(task), PENDING_TTL_MINUTES, TimeUnit.MINUTES);
        stringRedisTemplate.opsForSet().add(nestKey, String.valueOf(task.getDispatchRecordId()));
        stringRedisTemplate.expire(nestKey, PENDING_TTL_MINUTES, TimeUnit.MINUTES);
        log.info("XIANINGDRONE_CAPTURE_REDIS_SAVE_SUCCESS dispatchRecordId={} ttlMinutes={} distanceThresholdMeters={} speedThresholdCmps={}",
                 task.getDispatchRecordId(), PENDING_TTL_MINUTES, task.getArrivalDistanceThresholdMeters(), task.getArrivalSpeedThresholdCmps());
        try {
            log.info("XIANINGDRONE_CAPTURE_MQTT_SUBSCRIBE_BEGIN dispatchRecordId={} tenant={} nestSn={}", task.getDispatchRecordId(),
                     task.getTenant(), task.getNestSn());
            mqttClientManager.ensureSubscribed(task.getTenant(), task.getNestSn());
        } catch (RuntimeException e) {
            log.warn("XIANINGDRONE_CAPTURE_MQTT_SUBSCRIBE_FAIL dispatchRecordId={} tenant={} nestSn={}", task.getDispatchRecordId(),
                     task.getTenant(), task.getNestSn(), e);
            removePending(task);
            throw e;
        }
    }

    /**
     * 消费飞机状态消息：先按租户和机库取待拍摄任务，再对每条任务做到点判定。
     */
    @Override
    public void handleStatusMessage(Integer tenant, String nestSn, String payload) {
        if (tenant == null || !StringUtils.hasText(nestSn) || !StringUtils.hasText(payload)) {
            log.info("XIANINGDRONE_MQTT_STATUS_SKIP reason=invalid_input tenant={} nestSn={} payloadBlank={}", tenant, nestSn,
                     !StringUtils.hasText(payload));
            return;
        }
        DroneStatus status = parseStatus(nestSn, payload);
        if (status == null) {
            log.info("XIANINGDRONE_MQTT_STATUS_SKIP reason=parse_failed tenant={} nestSn={}", tenant, nestSn);
            return;
        }
        log.info("XIANINGDRONE_MQTT_STATUS_PARSED tenant={} nestSn={} lng={} lat={} horizontalSpeedCmps={}", tenant, nestSn,
                 status.longitude(), status.latitude(), status.horizontalSpeedCmps());
        Set<String> dispatchRecordIds = stringRedisTemplate.opsForSet().members(nestKey(tenant, nestSn));
        if (dispatchRecordIds == null || dispatchRecordIds.isEmpty()) {
            log.info("XIANINGDRONE_MQTT_STATUS_NO_PENDING tenant={} nestSn={}", tenant, nestSn);
            return;
        }
        log.info("XIANINGDRONE_MQTT_STATUS_PENDING_FOUND tenant={} nestSn={} pendingCount={}", tenant, nestSn, dispatchRecordIds.size());
        for (String dispatchRecordId : dispatchRecordIds) {
            DronePendingCaptureTask task = readTask(dispatchRecordId);
            if (task == null) {
                log.info("XIANINGDRONE_CAPTURE_PENDING_STALE tenant={} nestSn={} dispatchRecordId={}", tenant, nestSn, dispatchRecordId);
                stringRedisTemplate.opsForSet().remove(nestKey(tenant, nestSn), dispatchRecordId);
                continue;
            }
            ArrivalCheck arrivalCheck = arrivalCheck(task, status);
            log.info(
                    "XIANINGDRONE_ARRIVAL_CHECK dispatchRecordId={} tenant={} nestSn={} arrived={} distanceMeters={} distanceThresholdMeters={} horizontalSpeedCmps={} speedThresholdCmps={}",
                    task.getDispatchRecordId(), tenant, nestSn, arrivalCheck.arrived(), BigDecimal.valueOf(arrivalCheck.distanceMeters()),
                    task.getArrivalDistanceThresholdMeters(), status.horizontalSpeedCmps(), task.getArrivalSpeedThresholdCmps());
            if (!arrivalCheck.arrived()) {
                continue;
            }
            triggerOnce(task);
        }
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }

    /**
     * 将派飞日志和派飞配置压缩为 Redis 待拍摄任务快照，避免后续触发时依赖配置实时值。
     */
    private DronePendingCaptureTask buildTask(DroneAlarmDispatchRecord record, DroneAlarmDispatchConfig config) {
        DronePendingCaptureTask task = new DronePendingCaptureTask();
        task.setDispatchRecordId(record.getId());
        task.setTenant(record.getTenant());
        task.setNestSn(record.getNestSn());
        task.setTargetLongitude(record.getLongitude());
        task.setTargetLatitude(record.getLatitude());
        task.setMediaAction(record.getMediaAction());
        task.setGimbalYtType(config.getGimbalYtType() == null ? DEFAULT_GIMBAL_YT_TYPE : config.getGimbalYtType());
        task.setArrivalDistanceThresholdMeters(
                config.getArrivalDistanceThresholdMeters() == null ? DEFAULT_ARRIVAL_DISTANCE_THRESHOLD_METERS
                                                                   : config.getArrivalDistanceThresholdMeters());
        task.setArrivalSpeedThresholdCmps(config.getArrivalSpeedThresholdCmps() == null ? DEFAULT_ARRIVAL_SPEED_THRESHOLD_CMPS
                                                                                        : config.getArrivalSpeedThresholdCmps());
        task.setVideoDurationSeconds(
                config.getVideoDurationSeconds() == null ? DEFAULT_VIDEO_DURATION_SECONDS : config.getVideoDurationSeconds());
        return task;
    }

    /**
     * 解析 PDF 6.2 飞机状态字段；topic 和消息体 nestSn 不一致时丢弃，避免串机库触发。
     */
    private DroneStatus parseStatus(String topicNestSn, String payload) {
        try {
            JsonNode root = JsonUtils.deserialize(payload);
            if (root == null) {
                log.info("XIANINGDRONE_MQTT_STATUS_PARSE_FAIL reason=json_root_null topicNestSn={}", topicNestSn);
                return null;
            }
            String bodyNestSn = text(root.get("nestSn"));
            if (StringUtils.hasText(bodyNestSn) && !topicNestSn.equals(bodyNestSn)) {
                log.info("XIANINGDRONE_MQTT_STATUS_PARSE_FAIL reason=nest_sn_mismatch topicNestSn={} bodyNestSn={}", topicNestSn, bodyNestSn);
                return null;
            }
            BigDecimal longitude = decimal(root.get("longitude"));
            BigDecimal latitude = decimal(root.get("latitude"));
            BigDecimal horizontalSpeed = decimal(root.get("horizontalSpeed"));
            if (longitude == null || latitude == null || horizontalSpeed == null) {
                log.info("XIANINGDRONE_MQTT_STATUS_PARSE_FAIL reason=missing_required_fields topicNestSn={} lng={} lat={} speed={}",
                         topicNestSn, longitude, latitude, horizontalSpeed);
                return null;
            }
            return new DroneStatus(longitude, latitude, horizontalSpeed.abs());
        } catch (Exception e) {
            log.info("XIANINGDRONE_MQTT_STATUS_PARSE_FAIL reason=exception topicNestSn={} payload={}", topicNestSn, payload, e);
            return null;
        }
    }

    private DronePendingCaptureTask readTask(String dispatchRecordId) {
        if (!StringUtils.hasText(dispatchRecordId)) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().get(pendingKey(dispatchRecordId));
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return JsonUtils.deserialize(json, DronePendingCaptureTask.class);
    }

    /**
     * 到点条件：飞机位置到目标点距离小于阈值，且水平速度小于等于阈值。
     */
    private ArrivalCheck arrivalCheck(DronePendingCaptureTask task, DroneStatus status) {
        if (task.getTargetLongitude() == null || task.getTargetLatitude() == null
            || task.getArrivalDistanceThresholdMeters() == null || task.getArrivalSpeedThresholdCmps() == null) {
            return new ArrivalCheck(false, -1D);
        }
        double distance = distanceMeters(status.longitude(), status.latitude(), task.getTargetLongitude(), task.getTargetLatitude());
        boolean arrived = BigDecimal.valueOf(distance).compareTo(task.getArrivalDistanceThresholdMeters()) < 0
                          && status.horizontalSpeedCmps().compareTo(task.getArrivalSpeedThresholdCmps()) <= 0;
        return new ArrivalCheck(arrived, distance);
    }

    /**
     * 使用 Redis SET NX 锁保证同一派飞日志只触发一次拍摄，抵抗 MQTT 重复消息和多实例并发。
     */
    private void triggerOnce(DronePendingCaptureTask task) {
        Boolean locked = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey(task.getDispatchRecordId()), "1", LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(locked)) {
            log.info("XIANINGDRONE_CAPTURE_LOCK_SKIP dispatchRecordId={} reason=lock_not_acquired", task.getDispatchRecordId());
            return;
        }
        log.info("XIANINGDRONE_CAPTURE_LOCK_SUCCESS dispatchRecordId={} mediaAction={}", task.getDispatchRecordId(), task.getMediaAction());
        try {
            executeWithTenant(task.getTenant(), () -> {
                if (MEDIA_ACTION_PHOTO.equals(task.getMediaAction())) {
                    triggerPhoto(task);
                } else if (MEDIA_ACTION_VIDEO.equals(task.getMediaAction())) {
                    triggerVideo(task);
                }
                return null;
            });
            removePending(task);
        } catch (Exception e) {
            log.warn("XIANINGDRONE_CAPTURE_TRIGGER_FAIL dispatchRecordId={} mediaAction={}", task.getDispatchRecordId(), task.getMediaAction(), e);
            executeWithTenant(task.getTenant(), () -> {
                markFailed(task.getDispatchRecordId(), e.getMessage(), null);
                return null;
            });
            removePending(task);
        }
    }

    /**
     * 到点拍照：调用云台 photograph 指令，并把三方响应写回派飞日志。
     */
    private void triggerPhoto(DronePendingCaptureTask task) {
        Map<String, Object> request = commandRequest(task, GIMBAL_TYPE_PHOTOGRAPH, CATEGORY_START);
        log.info("XIANINGDRONE_PHOTO_BEGIN dispatchRecordId={} tenant={} nestSn={} ytType={}", task.getDispatchRecordId(), task.getTenant(),
                 task.getNestSn(), task.getGimbalYtType());
        String response = xianingDroneApiClient.gimbalControl(task.getNestSn(), task.getGimbalYtType(), GIMBAL_TYPE_PHOTOGRAPH, CATEGORY_START);
        markSuccess(task.getDispatchRecordId(), request, Map.of("photograph", response));
        log.info("XIANINGDRONE_PHOTO_SUCCESS dispatchRecordId={} nestSn={}", task.getDispatchRecordId(), task.getNestSn());
    }

    /**
     * 到点录像：先调用 video_start 并标记成功，再用本地延迟任务按配置时长关闭录像。
     */
    private void triggerVideo(DronePendingCaptureTask task) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("start", commandRequest(task, GIMBAL_TYPE_VIDEO_START, CATEGORY_START));
        request.put("end", commandRequest(task, GIMBAL_TYPE_VIDEO_END, CATEGORY_END));
        request.put("durationSeconds", task.getVideoDurationSeconds());
        log.info("XIANINGDRONE_VIDEO_START_BEGIN dispatchRecordId={} tenant={} nestSn={} ytType={} durationSeconds={}",
                 task.getDispatchRecordId(), task.getTenant(), task.getNestSn(), task.getGimbalYtType(), task.getVideoDurationSeconds());
        String startResponse = xianingDroneApiClient.gimbalControl(task.getNestSn(), task.getGimbalYtType(), GIMBAL_TYPE_VIDEO_START, CATEGORY_START);
        markSuccess(task.getDispatchRecordId(), request, Map.of("start", startResponse));
        int durationSeconds = task.getVideoDurationSeconds() == null ? DEFAULT_VIDEO_DURATION_SECONDS : task.getVideoDurationSeconds();
        scheduler.schedule(() -> stopVideo(task, startResponse), Math.max(1, durationSeconds), TimeUnit.SECONDS);
        log.info("XIANINGDRONE_VIDEO_START_SUCCESS dispatchRecordId={} nestSn={} stopAfterSeconds={}", task.getDispatchRecordId(),
                 task.getNestSn(), Math.max(1, durationSeconds));
    }

    /**
     * 延迟关闭录像；如果服务在延迟期间重启，本地任务不会恢复，需要人工或后续补偿处理。
     */
    private void stopVideo(DronePendingCaptureTask task, String startResponse) {
        try {
            executeWithTenant(task.getTenant(), () -> {
                log.info("XIANINGDRONE_VIDEO_END_BEGIN dispatchRecordId={} tenant={} nestSn={} ytType={}", task.getDispatchRecordId(),
                         task.getTenant(), task.getNestSn(), task.getGimbalYtType());
                String endResponse = xianingDroneApiClient.gimbalControl(task.getNestSn(), task.getGimbalYtType(), GIMBAL_TYPE_VIDEO_END,
                                                                         CATEGORY_END);
                DroneAlarmDispatchRecord update = new DroneAlarmDispatchRecord();
                update.setId(task.getDispatchRecordId());
                update.setMediaResponseBody(JsonUtils.serialize(Map.of("start", startResponse, "end", endResponse)));
                recordMapper.updateById(update);
                log.info("XIANINGDRONE_VIDEO_END_SUCCESS dispatchRecordId={} nestSn={}", task.getDispatchRecordId(), task.getNestSn());
                return null;
            });
        } catch (Exception e) {
            log.warn("XIANINGDRONE_VIDEO_END_FAIL dispatchRecordId={} nestSn={}", task.getDispatchRecordId(), task.getNestSn(), e);
            executeWithTenant(task.getTenant(), () -> {
                markFailed(task.getDispatchRecordId(), e.getMessage(), Map.of("start", startResponse));
                return null;
            });
        }
    }

    /**
     * MQTT 回调和延迟任务不在请求线程中，访问租户配置或更新租户表前需要补租户上下文。
     */
    private <T> T executeWithTenant(Integer tenant, TenantCallable<T> callable) {
        try {
            SessionContextHolder.requestTenant(tenant);
            return callable.call();
        } finally {
            SessionContextHolder.requestTenant(null);
        }
    }

    private void markSuccess(Long dispatchRecordId, Object request, Object response) {
        DroneAlarmDispatchRecord update = new DroneAlarmDispatchRecord();
        update.setId(dispatchRecordId);
        update.setMediaStatus(MEDIA_STATUS_SUCCESS);
        update.setMediaRequestBody(JsonUtils.serialize(request));
        update.setMediaResponseBody(JsonUtils.serialize(response));
        update.setMediaTriggerTime(LocalDateTime.now());
        recordMapper.updateById(update);
    }

    private void markFailed(Long dispatchRecordId, String reason, Object response) {
        DroneAlarmDispatchRecord update = new DroneAlarmDispatchRecord();
        update.setId(dispatchRecordId);
        update.setMediaStatus(MEDIA_STATUS_FAIL);
        update.setMediaFailReason(limit(reason));
        if (response != null) {
            update.setMediaResponseBody(JsonUtils.serialize(response));
        }
        update.setMediaTriggerTime(LocalDateTime.now());
        recordMapper.updateById(update);
    }

    private Map<String, Object> commandRequest(DronePendingCaptureTask task, String type, Integer category) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("sn", task.getNestSn());
        request.put("ytType", task.getGimbalYtType());
        request.put("type", type);
        request.put("category", category);
        return request;
    }

    /**
     * 清理 Redis 待拍摄任务，防止已触发任务继续被后续 MQTT 状态重复扫描。
     */
    private void removePending(DronePendingCaptureTask task) {
        stringRedisTemplate.delete(pendingKey(task.getDispatchRecordId()));
        stringRedisTemplate.opsForSet().remove(nestKey(task.getTenant(), task.getNestSn()), String.valueOf(task.getDispatchRecordId()));
        log.info("XIANINGDRONE_CAPTURE_REDIS_CLEAN dispatchRecordId={} tenant={} nestSn={}", task.getDispatchRecordId(), task.getTenant(),
                 task.getNestSn());
    }

    private String pendingKey(Long dispatchRecordId) {
        return pendingKey(String.valueOf(dispatchRecordId));
    }

    private String pendingKey(String dispatchRecordId) {
        return PENDING_KEY_PREFIX + dispatchRecordId;
    }

    private String nestKey(Integer tenant, String nestSn) {
        return NEST_TASK_KEY_PREFIX + tenant + ":" + nestSn;
    }

    private String lockKey(Long dispatchRecordId) {
        return LOCK_KEY_PREFIX + dispatchRecordId;
    }

    private double distanceMeters(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        double latRad1 = Math.toRadians(lat1.doubleValue());
        double latRad2 = Math.toRadians(lat2.doubleValue());
        double latDelta = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double lngDelta = Math.toRadians(lng2.doubleValue() - lng1.doubleValue());
        double a = Math.sin(latDelta / 2D) * Math.sin(latDelta / 2D)
                   + Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(lngDelta / 2D) * Math.sin(lngDelta / 2D);
        return EARTH_RADIUS_METERS * 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
    }

    private BigDecimal decimal(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? new BigDecimal(text) : null;
    }

    private String text(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? text : null;
    }

    private String limit(String reason) {
        if (!StringUtils.hasText(reason)) {
            return null;
        }
        return reason.length() > MAX_REASON_LENGTH ? reason.substring(0, MAX_REASON_LENGTH) : reason;
    }

    private record DroneStatus(BigDecimal longitude, BigDecimal latitude, BigDecimal horizontalSpeedCmps) {
    }

    private record ArrivalCheck(boolean arrived, double distanceMeters) {
    }

    @FunctionalInterface
    private interface TenantCallable<T> {
        T call();
    }

    private static class CaptureThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("drone-alarm-capture-video-stop");
            thread.setDaemon(true);
            return thread;
        }
    }
}
