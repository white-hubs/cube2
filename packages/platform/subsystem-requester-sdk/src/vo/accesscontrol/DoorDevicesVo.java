package com.guhecloud.subsystem.requester.sdk.vo.accesscontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorDevicesVo {
    //唯一编码
    private String indexCode;
    //资源类型
    private String resourceType;
    //资源名称
    private String name;
    //门禁设备类型编码
    private String devTypeCode;
    //门禁类型型号
    private String devTypeDesc;
    //主动设备编号
    private String deviceCode;
    //厂商
    private String manufacturer;
    //区域名称
    private String regionName;
    //设备序列号
    private String devSerialNum;
}
