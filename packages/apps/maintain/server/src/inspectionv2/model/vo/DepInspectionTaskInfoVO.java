package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 部门巡检任务信息
 * @date 2023/4/18 16:02
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepInspectionTaskInfoVO {
    /**
     * 部门名称
     */
    private String depName;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 任务总数
     */
    private Long totalTaskNum;
    /**
     * 正常完成任务数
     */
    private Long completeTaskNum;
    /**
     * 超时完成任务数
     */
    private Long overTimeTaskNum;
    /**
     * 过期未执行数
     */
    private Long expireTaskNum;
    /**
     * 任务总耗时
     */
    private Long taskTotalTime;
    /**
     * 任务总耗时字符串
     */
    private String taskTotalTimeStr;
    /**
     * 任务完成率
     */
    private String taskCompleteRatio;

}
