package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AlarmDeviceStatusStatisticsExportVO {
    /**
     * 报警设备code
     */
    @ExcelProperty(value = { "设备编码" }, index = 1)
    private String deviceCode;

    /**
     * 报警设备名称
     */
    @ExcelProperty(value = { "设备名称" }, index = 2)
    private String deviceName;
    /**
     * 档位code
     */
    @ExcelProperty(value = { "档位编码" }, index = 2)
    private String gearPositionCode;
    /**
     * 报警设备区域
     */
    @ExcelProperty(value = { "档位区域" }, index = 3)
    private String areaName;

    /**
     * 报警总数
     */
    @ExcelProperty(value = { "报警总数" }, index = 4)
    private Long totalNum;

    /**
     * 待接收报警数
     */
    @ExcelProperty(value = { "待接收报警数" }, index = 5)
    private Long unConfirmedNum;

    /**
     * 处理中报警数
     */
    @ExcelProperty(value = { "处理中报警数" }, index = 6)
    private Long progressConfirmedNum;

    /**
     * 已处理报警数
     */
    @ExcelProperty(value = { "已处理报警数" }, index = 7)
    private Long handlerNum;

    /**
     * 已挂起报警数
     */
    @ExcelProperty(value = { "已挂起报警数" }, index = 8)
    private Long suspendNum;

    /**
     * 超时报警数
     */
    @ExcelProperty(value = { "超时报警数" }, index = 9)
    private Long timeoutNum;
}
