package com.guhecloud.subsystem.config.listener.nacos.enumeration;

import lombok.Getter;

/**
 * 服务端点信息
 *
 * @author lanchun
 * @date 2022/9/19 10:06
 */
@Getter
public enum EndpointType {

    /**
     * 服务名
     */
    SERVICE_NAME,

    /**
     * id地址
     */
    IP_ADDRESS
}
