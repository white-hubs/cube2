package com.guhecloud.cube.base.flow.work.order.statistics.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/21 10:17
 * @Description: 工单中心统计结果
 */
@Data
public class WorkOrderStatisticsDTO1 {

    /**
     * 类型编码
     */
    @ExcelIgnore
    private String businessType;
    /**
     * 工单类型
     */
    @ExcelProperty("工单类型")
    private String businessTypeName;
    /**
     * 工单分组名称
     */
    @ExcelProperty("工单分组")
    private String formGroupName;
    /**
     * 总数
     */
    @ExcelProperty("工单总数")
    private Integer totalNumber;
    /**
     * 处理中工单数
     */
    @ExcelProperty("处理中工单数")
    private Integer processNumber;
    /**
     * 驳回工单数
     */
    @ExcelProperty("驳回工单数")
    private Integer rejectNumber;
    /**
     * 驳回工单数
     */
    @ExcelProperty("撤销工单数")
    private Integer undoNumber;

    /**
     * 驳回工单数
     */
    @ExcelProperty("完成工单数")
    private Integer dealNumber;

}
