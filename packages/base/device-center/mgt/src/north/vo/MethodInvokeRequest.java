package com.guhecloud.cube.iot.mgt.north.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 方法调用请求参数
 */
@Data
public class MethodInvokeRequest {

    /**
     * 命令序列号(可选,由客户端指定或系统自动生成)
     */
    private String commandSeq;

    /**
     * 设备ID
     */
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;

    /**
     * 方法ID
     */
    @NotNull(message = "方法ID不能为空")
    private String methodCode;

    /**
     * 输入参数(JSON格式的Map)
     */
    private Map<String, Object> inputParams;
}
