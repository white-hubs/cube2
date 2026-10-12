package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备维保统计
 *
 * @author lanchun
 * @date 2025/4/15 19:27
 */
@Data
public class MaintenanceDeviceStatisticsPageVO {

    /**
     * 设备id
     */
    @ExcelIgnore
    private Long id;

    /**
     * 设备名称
     */
    @ExcelProperty("设备名称")
    private String name;

    /**
     * 品类编码
     */
    @ExcelIgnore
    private String categoryCode;

    /**
     * 品类名称
     */
    @ExcelProperty("产品品类")
    private String categoryName;

    /**
     * 维保次数
     */
    @ExcelProperty("维保次数")
    private Integer num = 0;

    /**
     * 最近维保时间
     */
    @ExcelProperty("最近维保时间")
    private LocalDateTime lastTime;
}
