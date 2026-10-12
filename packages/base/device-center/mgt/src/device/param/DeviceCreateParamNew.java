package com.guhecloud.cube.iot.mgt.device.param;

import com.guhecloud.cube.iot.mgt.comm.excel.ExcelColumn;
import com.guhecloud.cube.iot.mgt.comm.excel.ExcelSheet;
import lombok.Data;

@Data
@ExcelSheet(name = "设备导入模板")
public class DeviceCreateParamNew {
    @ExcelColumn(name = "聚合组名称(设备名称)", width = 30)
    private String deviceName;
    @ExcelColumn(name = "聚合组编码(设备编码)", width = 30)
    private String deviceNumber;
    @ExcelColumn(name = "第三方设备id", width = 64)
    private String deviceIdentifier;
}
