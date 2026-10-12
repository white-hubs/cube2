package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

/**
 * @author wql
 * @describe 巡更人员统计
 * @date 2023/5/30 14:40
 */
@Data
public class PersonnelStatisticsVO {
    /**
     * 巡更人员
     */
    private String userName;
    /**
     * 巡更人员id
     */
    @ExcelIgnore
    private Long userId;
    /**
     * 所属部门
     */
    private String depName;
    /**
     * 所属部门id
     */
    @ExcelIgnore
    private Long depId;
    /**
     * 巡更任务数
     */
    private Integer taskNum;
    /**
     * 异常任务数
     */
    private Integer abnormalTaskNum;
    /**
     * 巡更点总数
     */
    private Long totalNum;
    /**
     * 已巡更点数
     */
    private Long realNum;
    /**
     * 漏巡点数
     */
    private Long omitNum;
    /**
     * 巡更率
     */
    private String patrolRatio;
}
