package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class InspectStatOverviewVO {

    /**
     * 计划数
     */
    private int planNum;

    /**
     * 任务数
     */
    private int taskNum;

    /**
     * 任务ID
     */
    private List<Long> taskIds;

    /**
     * 记录数
     */
    private int recordNum;

    /**
     * 记录异常数
     */
    private long recordAbnormalNum;

    /**
     * 记录完成数
     */
    private long recordOverNum;

    /**
     * 记录未巡检数
     */
    private long recordExecutingNum;

    /**
     * 记录过期数
     */
    private long recordExpireNum;

    /**
     * 记录停止数
     */
    private long recordStopNum;

}
