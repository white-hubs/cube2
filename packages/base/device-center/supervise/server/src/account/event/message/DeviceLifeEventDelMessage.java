package com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message;

import lombok.Data;

@Data
public class DeviceLifeEventDelMessage {

    public final static String TOPIC = "DEVICE_LIFE_EVENT_DEL";

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 事件ID
     */
    private Long eventId;

}
