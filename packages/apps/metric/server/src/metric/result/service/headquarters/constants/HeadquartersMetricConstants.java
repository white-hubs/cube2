package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.constants;

public class HeadquartersMetricConstants {

    /**
     * 报警总数接口地址
     */
    public static final String ALARM_NUMBER_REQUEST_URL = "/alarm/alarm-statistics/ring-ratio-analysis";

    /**
     * 报警级别数量接口地址
     */
    public static final String ALARM_LEVEL_NUMBER_REQUEST_URL = "/alarm/alarm-statistics/level";

    /**
     * 报警状态数量接口地址
     */
    public static final String ALARM_STATUS_NUMBER_REQUEST_URL = "/alarm/alarm-statistics/status-group";

    /**
     * 报警级别处理率接口地址
     */
    public static final String ALARM_LEVEL_HANDLER_RATE_REQUEST_URL = "/alarm/alarm-statistics/level/handler-rate/statistics";

    /**
     * 报警平均处理效率接口地址
     */
    public static final String ALARM_HANDLER_EFFICIENCY_REQUEST_URL = "/alarm/alarm-statistics/avg-handler-efficiency";

    /**
     * 报警级别平均处理效率统计接口地址
     */
    public static final String ALARM_LEVEL_AVG_HANDLER_EFFICIENCY_REQUEST_URL = "/alarm/alarm-statistics/level/avg-handler-efficiency";

    /**
     * 报警事件分类处理情况列表接口地址
     */
    public static final String ALARM_EVENT_TYPE_NUM_REQUEST_URL = "/alarm/alarm-bim-statistics/event-type/handler/search";

    /**
     * 报警结果统计接口地址
     */
    public static final String ALARM_IMPRACTICAL_RATE_REQUEST_URL = "/alarm/alarm-bim-result/statistics";

    /**
     * 用户报警处理率排名接口
     */
    public static final String ALARM_USER_HANDLER_RATE_TOP_REQUEST_URL = "/alarm/alarm-statistics/user/handler-rate/top";

    /**
     * 用户报警处理率倒数排名接口
     */
    public static final String ALARM_USER_HANDLER_RATE_LAST_TOP_REQUEST_URL = "/alarm/alarm-statistics/user/handler-rate/last-top";

    /**
     * 用户报警处理效率排名接口
     */
    public static final String ALARM_USER_HANDLER_EFFICIENCY_TOP_REQUEST_URL = "/alarm/alarm-statistics/user-handler-efficiency-top";

    /**
     * 用户报警处理效率排名接口
     */
    public static final String ALARM_USER_HANDLER_EFFICIENCY_LAST_TOP_REQUEST_URL = "/alarm/alarm-statistics/user-handler-efficiency-last-top";

    /**
     * 报警级别下用户处理效率接口
     */
    public static final String ALARM_LEVEL_USER_HANDLER_EFFICIENCY_REQUEST_URL = "/alarm/alarm-statistics/level-handler-efficiency-analysis";

    /**
     * 设备状态数量接口
     */
    public static final String DEVICE_STATUS_NUMBER_REQUEST_URL = "/device-center/device/tagCode/status/count";

    /**
     * 设备类型数量统计
     */
    public static final String DEVICE_TYPE_NUMBER_REQUEST_URL = "/device-center/device/product-category/status/count";

    /**
     * 设备到期数统计统计接口
     */
    public static final String DEVICE_EXPIRE_RATE_REQUEST_URL = "/device-center/device/expired/statistics";

    /**
     * 获取设备标签的平均健康值接口
     */
    public static final String DEVICE_HEALTH_AVG_LEVEl_REQUEST_URL = "/device-center/supervise//device/instance/analysis/health/avg-value";

    /**
     * 隐患排查数量统计接口
     */
    public static final String HIDDEN_DANGER_NUMBER_REQUEST_URL = "/maintain/hidden/num/statistics";

    /**
     * 工单完成率排名接口
     */
    public static final String WORK_ORDER_COMPLETE_RATE_TOP_REQUEST_URL = "/maintain/hidden/work/order/statistics/all";

    /**
     * 故障报修单数据统计接口
     */
    public static final String FAULT_REPAIR_NUMBER_REQUEST_URL = "/maintain/fault/completed/metric";

    /**
     * 电子巡更异常率接口
     */
    public static final String PATROL_EXCEPTION_RATE_REQUEST_URL = "/maintain/electron/statistics/errRate";

    /**
     * 电子巡更完成率接口
     */
    public static final String PATROL_COMPLETE_RATE_REQUEST_URL = "/maintain/electron/statistics/completeRate";

    /**
     * 电子巡更总数接口
     */
    public static final String PATROL_TOTAL_NUMBER_REQUEST_URL = "/maintain/electron/statistics/taskTotal";

    /**
     * 电子巡更处理率排名接口
     */
    public static final String PATROL_HANDLER_RATE_TOP_REQUEST_URL = "/maintain/electron/statistics/complete/rate/metric";

    /**
     * 电子巡更处理效率排名接口
     */
    public static final String PATROL_HANDLER_EFFICIENCY_TOP_REQUEST_URL = "/maintain/electron/statistics/complete/efficiency/metric";

    /**
     * 电子巡更处理市场和总数接口
     */
    public static final String PATROL_HANDLER_NUMBER_TIME_REQUEST_URL = "/maintain/electron/statistics/complete/total/time";

    /**
     * 日常巡检记录异常率接口
     */
    public static final String INSPECTION_EXCEPTION_RATE_REQUEST_URL = "/maintain/inspectRecord/errRate";

    /**
     * 日常巡检记录完成率接口
     */
    public static final String INSPECTION_COMPLETE_RATE_REQUEST_URL = "/maintain/inspectRecord/completeRate";

    /**
     * 日常巡检总数接口
     */
    public static final String INSPECTION_TOTAL_NUMBER_REQUEST_URL = "/maintain/inspect-task/taskTotal";

    /**
     * 日常巡检处理率排名接口
     */
    public static final String INSPECTION_HANDLER_RATE_TOP_REQUEST_URL = "/maintain/inspect-task/complete/rate/metric";

    /**
     * 日常巡检处理效率排名接口
     */
    public static final String INSPECTION_HANDLER_EFFICIENCY_TOP_REQUEST_URL = "/maintain/inspect-task/complete/efficiency/metric";

    /**
     * 日常巡检处理数和处理总时长
     */
    public static final String INSPECTION_HANDLER_NUMBER_TIME_REQUEST_URL = "/maintain/inspect-task/complete/time/num/metric";

    /**
     * 日常巡检次数指标
     */
    public static final String INSPECTION_RECORD_USER_NUMBER_REQUEST_URL = "/maintain/inspect-record/user/complete/num";

    /**
     * 小散工程指标接口
     */
    public static final String PROJECT_SCHEDULE_METRIC_REQUEST_URL = "/loose-works/project/schedule/metric";

    /**
     * 报警总处理超时率
     */
    public static final String ALARM_HANDLER_TIMEOUT_RATE_REQUEST_URL = "/alarm/alarm-bim-statistics/handler/timeout/rate";

    /**
     * 按用户统计报警处理超时率
     */
    public static final String ALARM_USER_HANDLER_TIMEOUT_RATE_REQUEST_URL = "/alarm/alarm-bim-statistics/users/handler/timeout/rate";

    /**
     * 报警级别统计
     */
    public static final String ALARM_LEVEL_STATISTICS_REQUEST_URL = "/alarm/alarm/alarm-statistics/level/top";

    private HeadquartersMetricConstants() {
    }
}
