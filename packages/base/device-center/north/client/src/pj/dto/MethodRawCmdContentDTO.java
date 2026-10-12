package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 方法调用状态详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodRawCmdContentDTO {
    /**
     * 命令实例ID
     */
    private String instanceId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 方法编码（产品内唯一，用于跨环境映射）
     */
    private String methodCode;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法类型：sync-同步方法、async-异步方法
     */
    private String methodType;

    /**
     * 输入参数定义
     */
    private Map<String, Object> inputParams;

    /**
     * 输出参数定义
     */
    private Map<String, Object> outputParams;
}
