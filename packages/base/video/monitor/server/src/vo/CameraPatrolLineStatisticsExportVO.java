package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线路统计导出返回参数
 *
 * @author lanchun
 * @date 2022/11/9 11:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class CameraPatrolLineStatisticsExportVO {

    /**
     * 名称
     */
    @ExcelProperty(value = { "巡逻路线" }, index = 0)
    private String name;

    /**
     * 监控数量
     */
    @ExcelProperty(value = { "监控点数" }, index = 1)
    private Integer cameraNum;

    /**
     * 巡逻人员名称
     */
    @ExcelProperty(value = { "巡逻人员" }, index = 2)
    private String patrolUserNames;

    /**
     * 巡逻次数
     */
    @ExcelProperty(value = { "巡逻次数" }, index = 3)
    private Integer patrolNum;

    /**
     * 巡逻异常数量
     */
    @ExcelProperty(value = { "异常总数" }, index = 4)
    private Integer abnormalNum;
}
