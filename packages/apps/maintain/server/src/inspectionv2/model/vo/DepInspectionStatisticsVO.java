package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 部门巡检统计
 * @date 2023/4/17 13:32
 */
@Data
public class DepInspectionStatisticsVO {
    /**
     * 所属部门id
     */
    private Long depId;
    /**
     * 所属部门
     */
    private String depName;
    /**
     * 覆盖对象数
     */
    private Long coverObjNum;
    /**
     * 对象覆盖率
     */
    private String coverObjRatio;
    /**
     * 任务总数
     */
    private Long taskTotalNum;
    /**
     * 完成任务数
     */
    private Long completeTaskNum;
    /**
     * 过期未执行任务数
     */
    private Long expireTaskNum;
    /**
     * 任务完成率
     */
    private String taskCompleteRatio;
    /**
     * 巡检次数
     */
    private Integer inspectionNum;
    /**
     * 巡检耗时
     */
    private Long inspectionElapsedTime;
    /**
     * 巡检耗时字符串
     */
    private String inspectionElapsedTimeStr;
    /**
     * 稽查平均匹配率
     */
    private String checkMatchRatio;
    /**
     * 异常隐患数
     */
    private Integer abnormalHiddenDangerNum;
    /**
     * 已办理隐患数
     */
    private Integer haveDoneHiddenDangerNum;
    /**
     * 隐患处理率
     */
    private String hiddenDangerDisposeRatio;
    /**
     * 有效隐患占比
     */
    private String validHiddenDangerRatio;
    /**
     * 参数巡检人数
     */
    private Integer personNum;
}
