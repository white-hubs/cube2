package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.Map;

/**
 * 方法调用请求参数
 */
@Data
public class MethodInvokeRequestDTO {

    /**
     * 命令序列号(可选,由客户端指定或系统自动生成)
     */
    private String commandSeq;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 方法编码
     */
    private String methodCode;

    /**
     * 输入参数(JSON格式的Map)
     */
    private Map<String, Object> inputParams;
}
