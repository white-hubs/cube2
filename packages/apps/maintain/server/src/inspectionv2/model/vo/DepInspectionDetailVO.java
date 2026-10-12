package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 巡检记录明细
 * @date 2023/4/17 20:01
 */
@Data
public class DepInspectionDetailVO {
    /**
     * 发布时间
     */
    private String releaseTime;
    /**
     * 巡检对象名称
     */
    private String objName;
    /**
     * 巡检对象id
     */
    private Long objId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 父任务id
     */
    private Long parentId;
    /**
     * 巡检人员
     */
    private String inspectionStaff;
    /**
     * 稽查人员
     */
    private String checkStaff;
    /**
     * 状态
     */
    private String state;
    /**
     * 巡检时间
     */
    private String inspectionTime;
    /**
     * 结果
     */
    private String result;
}
