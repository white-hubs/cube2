package com.guhecloud.cube.srv.gen.reportmanagement.server.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作汇报统计结果
 */

@Data
public class ReportStatistics {
    /**
     * 人员id
     */
    private String userId;
    /**
     * 人员名称
     */
    @ExcelProperty(value = { "人员" })
    private String userName;
    /**
     * 部门id
     */
    private Long organizationId;
    /**
     * 部门名称
     */
    @ExcelProperty(value = { "部门" })
    private String departmentName;
    /**
     * 部门汇报数
     */
    @ExcelProperty(value = { "部门汇报数" })
    private Long departmentNum;

    /**
     * 个人汇报数
     */
    @ExcelProperty(value = { "个人汇报数" })
    private Long userNum;

    /**
     * 各类型汇报集合
     */
    private List<ReportTypeNum> reportTypeNumList;
}
