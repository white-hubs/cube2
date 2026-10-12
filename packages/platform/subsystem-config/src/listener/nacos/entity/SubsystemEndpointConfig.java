package com.guhecloud.subsystem.config.listener.nacos.entity;

import com.guhecloud.subsystem.config.listener.nacos.enumeration.EndpointType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置信息
 *
 * @author lanchun
 * @date 2022/9/13 11:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubsystemEndpointConfig {

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 端点类型
     */
    private EndpointType endpointType;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务地址
     */
    private String serviceAddr;

    /**
     * 服务端口
     */
    private String servicePort;

    /**
     * 服务路径前缀
     */
    private String serviceAddrSuffix;

    /**
     * 子系统编码
     */
    private String subsystemCode;

    /**
     * 所属厂商
     */
    private String manufacturer;
}
