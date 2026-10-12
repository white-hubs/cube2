package com.guhecloud.cube.biz.door.server.param;

import lombok.Data;

@Data
public class DoorDeviceParam {
    //设备id
    private Long deviceId;
    //门禁类型
    private Long type;
    //设备编码
    private String doorCode;
}
