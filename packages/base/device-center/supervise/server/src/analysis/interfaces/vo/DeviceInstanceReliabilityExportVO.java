package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/8/3 15:45
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
public class DeviceInstanceReliabilityExportVO {
    @ExcelProperty(value = { "设备编码" }, index = 0)
    private String code;
    @ExcelProperty(value = { "设备名称" }, index = 1)
    private String name;
    @ExcelProperty(value = { "工作天数" }, index = 2)
    private Long workerDay;
    @ExcelProperty(value = { "设备故障次数" }, index = 3)
    private Integer troubleRunNumber;
}
