package com.guhecloud.cube.biz.door.server.param;

import lombok.Data;

@Data
public class DoorMonitorParam {
    //门禁控制信息
    private String monitorName;
    //门禁组态链接
    private String monitorUrl;
}
