package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

@Data
public class InspectStatByTargetVO {

    /**
     * 计划数
     */
    private long planNum;

    /**
     * 任务数
     */
    private long taskNum;

    /**
     * 记录异常数
     */
    private long abnormalNum;

}
