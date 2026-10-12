package com.guhecloud.cube.customization.xialing.server.drone.service;

import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchConfig;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneAlarmDispatchRecord;

/** 报警派飞到点拍摄任务服务。 */
public interface DroneAlarmDispatchCaptureService {
    /**
     * 派飞成功后注册待到点拍摄任务。
     */
    void register(DroneAlarmDispatchRecord record, DroneAlarmDispatchConfig config);

    /**
     * 处理 MQTT 飞机状态消息。
     */
    void handleStatusMessage(Integer tenant, String nestSn, String payload);
}
