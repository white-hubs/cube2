package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo;

import lombok.Data;

@Data
public class InspectPlanStatisticsVO {

    /**
     * 名称。
     */
    private String name;

    /**
     * 巡检类型 id。
     */
    private Long typeId;

    /**
     * 巡检类型。
     */
    private String typeName;

    /**
     * 任务数。
     */
    private Long taskTotal;

    /**
     * 已完成任务数。
     */
    private Long completedTaskTotal;

    /**
     * 待完成任务数。
     */
    private Long pendingTaskTotal;

    /**
     * 对象总数。
     */
    private Long objectTotal;

    /**
     * 已巡检对象数。
     */
    private Long inspectedObjectTotal;

    /**
     * 待巡检对象数。
     */
    private Long pendingObjectTotal;

    /**
     * 异常巡检项数。
     */
    private Long abnormalItemTotal;

    /**
     * 未处理异常项数。
     */
    private Long unhandledAbnormalItemTotal;

    /**
     * 已处理异常项数。
     */
    private Long handledAbnormalItemTotal;
}
