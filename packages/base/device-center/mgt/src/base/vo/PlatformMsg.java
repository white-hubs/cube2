package com.guhecloud.cube.iot.mgt.base.vo;

import lombok.Data;

/**
 * IOT平台中间件消息
 */
@Data
public class PlatformMsg {
    private String payload;
    private String requester;
}
