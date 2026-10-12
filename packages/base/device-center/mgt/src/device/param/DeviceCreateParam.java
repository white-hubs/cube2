package com.guhecloud.cube.iot.mgt.device.param;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class DeviceCreateParam {
    private Long productId;
    private Long parentDeviceId;
    @ExcelProperty("聚合组编码(设备编码)")
    @ColumnWidth(30)
    private String deviceNumber;
    @ExcelProperty("聚合组名称(设备名称)")
    @ColumnWidth(30)
    private String deviceName;

    //设备标识（边缘侧设备标识）
    private String deviceIdentifier;

    //三方设备id
    private String rawDeviceId;

    //设备类型 1真实设备; 0虚拟设备
    private int deviceType;

    //描述
    private String remark;

    //关联子设备ID集合
    private String relationDeviceIds;
}
