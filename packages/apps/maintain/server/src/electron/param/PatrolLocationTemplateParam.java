package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-14  09:55
 * @Description:
 * @Version: 1.0
 */
@Data
public class PatrolLocationTemplateParam {
    @ExcelProperty(value = "*巡更点名称")
    private String patrolLocationName;
    @ExcelProperty(value = "*所属区域")
    private String areaName;
    @ExcelProperty(value = "模型编码")
    private String bimCode;
    @ExcelProperty(value = "关联的巡更点设备")
    private String deviceCode;
    @ExcelProperty(value = "说明")
    private String remarks;
}
