package com.guhecloud.cube.customization.xialing.server.drone.service;

import com.guhecloud.cube.base.alarm.client.message.AlarmRecordAssignedMessage;
import com.guhecloud.cube.base.alarm.client.message.AlarmRecordOccurredMessage;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneAlarmDispatchConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneAlarmDispatchRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.drone.vo.DroneAlarmDispatchConfigVO;
import com.guhecloud.cube.customization.xialing.server.drone.vo.DroneAlarmDispatchRecordVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 无人机报警自动派飞服务。
 */
public interface DroneAlarmDispatchService {
    /**
     * 查询当前租户的唯一报警派飞配置。
     *
     * @return 当前配置；未配置时返回默认配置视图
     */
    DroneAlarmDispatchConfigVO currentConfig();

    /**
     * 保存当前租户的唯一报警派飞配置。
     *
     * @param param 配置保存参数
     *
     * @return 是否保存成功
     */
    Boolean saveCurrentConfig(DroneAlarmDispatchConfigSaveParam param);

    /**
     * 分页查询报警派飞日志。
     *
     * @param param 查询条件
     *
     * @return 派飞日志分页结果
     */
    Pagination<DroneAlarmDispatchRecordVO> pageRecords(DroneAlarmDispatchRecordPageParam param);

    /**
     * 查询报警派飞日志详情。
     *
     * @param id 派飞日志主键
     *
     * @return 派飞日志详情
     */
    DroneAlarmDispatchRecordVO recordDetail(Long id);

    /**
     * 处理报警发生消息，命中配置时触发无人机快速任务。
     *
     * @param message 报警发生消息
     */
    void handleAlarmOccurred(AlarmRecordOccurredMessage message);

    /**
     * 处理报警初始化指派完成消息，存在派飞记录时发送空地联动结果通知。
     *
     * @param message 报警初始化指派完成消息
     */
    void handleAlarmAssigned(AlarmRecordAssignedMessage message);
}
