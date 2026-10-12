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

import java.time.LocalDateTime;

/**
 * 视频巡逻记录分页返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class CameraPatrolRecordExportVO {

    /**
     * 巡逻时间
     */
    @ExcelProperty(value = { "巡逻时间" }, index = 0)
    private LocalDateTime patrolTime;

    /**
     * 巡逻线路名称
     */
    @ExcelProperty(value = { "巡逻路线名称" }, index = 1)
    private String cameraPatrolLineName;

    /**
     * 巡逻人员名称
     */
    @ExcelProperty(value = { "巡逻人员" }, index = 2)
    private String userName;

    /**
     * 巡逻耗时
     */
    @ExcelProperty(value = { "巡逻耗时" }, index = 3)
    private String duration;

    /**
     * 巡逻总数
     */
    @ExcelProperty(value = { "巡逻点数" }, index = 4)
    private Integer num;

    /**
     * 巡逻完成数量
     */
    @ExcelProperty(value = { "本次巡逻点数" }, index = 5)
    private Integer completeNum;

    /**
     * 巡逻异常数量
     */
    @ExcelProperty(value = { "异常数" }, index = 6)
    private Integer abnormalNum;
}