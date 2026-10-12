package com.guhecloud.cube.biz.door.server.vo;

import lombok.Data;

@Data
public class DoorMonitorVo {
    private Long id;
    //控制信息
    private String monitorName;
    //控制组态链接
    private String monitorUrl;
}
