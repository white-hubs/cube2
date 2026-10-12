package com.guhecloud.cube.base.flow.work.order.statistics.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  15:18
 * @Description:
 * @Version: 1.0
 */
@Data
public class WordOrderStatisticsDTO {
    /**
     * 用户id
     */
    @ExcelIgnore
    private String userId;
    /**
     * 用户名
     */
    @ExcelProperty("人员")
    private String userName;
    /**
     * 部门名称
     */
    @ExcelProperty("所属部门")
    private String organizationName;
    /**
     * 总数
     */
    @ExcelProperty("工单数")
    private Integer totalNumber;
    /**
     * 待处理数
     */
    @ExcelProperty("待处理")
    private Integer waitNumber;
    /**
     * 已处理数
     */
    @ExcelProperty("已处理")
    private Integer dealNumber;
    /**
     * 处理率
     */
    @ExcelProperty("处理率")
    private String dealRate;
    /**
     * 平均处理时长
     */
    @ExcelProperty("平均处理时长")
    private String avgWaitTime;
    /**
     * 总时长(分钟)
     */
    @ExcelIgnore
    private Integer totalWaitTime;

    public void getExtend() {
        if (totalNumber == null || totalNumber.equals(0)) {
            this.dealRate = "100%";
        } else if (dealNumber != null && dealNumber > 0) {
            double rate = ((double) dealNumber / (double) totalNumber) * 100;
            String s = String.format("%.2f", rate);
            this.dealRate = s + "%";
        } else {
            this.dealRate = "0%";
        }
        // 计算平均处理时长转为x小时xx分钟：总时长/总数*60
        if (totalWaitTime != null && totalNumber != 0) {
            Integer avg = totalWaitTime / totalNumber;
            avgWaitTime = String.format("%d小时%d分钟", avg / 60, avg % 60);
        }
    }
}
