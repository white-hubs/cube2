package com.guhecloud.cube.base.dataintegrate.server.enumeration;

import lombok.Getter;

/**
 * 采集job定义
 * 注意 如果是公网访问 地址需要加上前缀： /api/网关的服务名
 */
public enum JobHandlerEnum {

    ALARM("AlarmSyncJobHandler", "/alarm/alarm-standard-records", "POST", "报警数据采集", "0 0/1 * * * ?"),

    INSPECT("InspectSyncJobHandler", "/maintain/inspect-task/getInspectDataModel", "GET", "巡检数据采集", "0 0/1 * * * ?"),

    HIDDEN_DANGER("HiddenDangerSyncJobHandler", "/maintain/hidden-danger-headquarters-ather", "GET", "隐患数据采集", "0 0/1 * * * ?"),

    PATROL("PatrolSyncJobHandler", "/maintain/electron/task/getInspectDataModel", "GET", "巡更数据采集", "0 0/1 * * * ?"),

    CULTIVATE("CultivateSyncJobHandler", "/safety-education/cultivate-task/standard/list", "GET", "培训数据采集", "0 0/1 * * * ?"),

    EXAM("ExamSyncJobHandler", "/safety-education/exam-tasks", "GET", "考试数据采集", "0 0/1 * * * ?"),

    DEVICE("DeviceSyncJobHandler", "/device-center/device-headquarters-Gather", "GET", "设备数据采集", "0 0/1 * * * ?"),

    LOOSE_WORK("LooseWorkSyncJobHandler", "/loose-works/project/schedule/dataCollect", "GET", "小散数据采集", "0 0/1 * * * ?"),

    EMERGENCY_FILE("EmergencyFileSyncJobHandler", "/emergency/emergency-files/dataCollect", "GET", "预案数据采集", "0 0/1 * * * ?"),

    PROPERTY_RISK("PropertyRiskSyncJobHandler", "/property/risk/source/ledger/queryAll", "GET", "资产风险采集", "0 0/1 * * * ?"),

    SAFETY_RISK_INDEX("SafetyRiskIndexSyncJobHandler", "/alarm/risk-index/detail", "GET", "安全风险指数得分采集", "0 30 0/1 ? * 2"),

    TEMP_INSPECT_RECORD("InspectRecordSyncJobHandler", "/maintain/inspect-record/getInspectDataModel", "GET", "临时巡检记录采集", "0 0/10 * * * ?"),

    ABNORMAL_TAG_RECORD_ITEM("InspectRecordItemSyncJobHandler", "/maintain/inspect-record/getInspectItemDataModel", "GET", "异常tag巡检记录项采集", "0 0/10 * * * ?"),

    ;

    @Getter
    String handler;
    @Getter
    String cron;
    @Getter
    String defaultPath;
    @Getter
    String methodType;
    @Getter
    String jobName;

    JobHandlerEnum(String handler, String defaultPath, String methodType, String jobName, String cron) {
        this.handler = handler;
        this.defaultPath = defaultPath;
        this.methodType = methodType;
        this.jobName = jobName;
        this.cron = cron;
    }

}
