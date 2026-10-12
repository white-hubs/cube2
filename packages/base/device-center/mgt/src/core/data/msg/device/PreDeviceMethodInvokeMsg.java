package com.guhecloud.cube.iot.mgt.core.data.msg.device;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 设备方法调用消息
 */
@Data
@Builder
public class PreDeviceMethodInvokeMsg {

    /**
     * 消息ID,通常是commandSeq
     */
    private String commandSeq;

    /**
     * 命令实例ID
     */
    private String instanceId;

    /**
     * 父设备ID(可为空)
     */
    private String parentDeviceId;

    /**
     * 父设备编号(可为空)
     */
    private String parentDeviceNumber;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceNumber;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 协议ID
     */
    private Integer protocolId;

    /**
     * 方法编码
     */
    private String methodCode;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法类型(sync/async)
     */
    private String methodType;

    /**
     * 输入参数
     */
    private Map<String, Object> inputParams;

    /**
     * 超时时间(毫秒)
     */
    private Integer timeoutMs;

    /**
     * 消息时间戳
     */
    private Long ts;
}
