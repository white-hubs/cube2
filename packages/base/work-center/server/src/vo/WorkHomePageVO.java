package com.guhecloud.cube.biz.work.center.server.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/4 11:50
 * @Description:
 */
@Data
public class WorkHomePageVO {

    /**
     * 工作事项总数
     */
    private Long totalNum = 0L;

    /**
     * 已完成事项总数
     */
    private Long doneTotalNum = 0L;
    /**
     * 普通任务数
     */
    private Long normalNum = 0L;

    /**
     * 普通任务完成数据
     */
    private Long doneNormalNum = 0L;
    /**
     * 报警任务数
     */
    private Long alarmNum = 0L;

    /**
     * 报警任务完成数据
     */
    private Long doneAlarmNum = 0L;

    /**
     * 审批任务数
     */
    private Long approveNum = 0L;

    /**
     * 审批任务完成数据
     */
    private Long doneApproveNum = 0L;

}
