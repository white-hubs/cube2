package com.guhecloud.cube.biz.door.server.vo;

import lombok.Data;

@Data
public class DoorEventsVo {
    //门禁名称
    private String doorName;
    //事件时间
    private String eventTime;
    //时间名称
    private String eventName;
}
