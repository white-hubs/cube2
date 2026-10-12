package com.guhecloud.cube.iot.mgt.north.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Map;

/**
 * 方法调用历史记录VO
 */
@Data
public class MethodInvokeHistoryVo {

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
     * 方法名称
     */
    private String methodName;

    /**
     * 方法编码
     */
    private String methodCode;

    /**
     * 输入参数
     */
    private Map<String, Object> inputParams;

    /**
     * 输出参数
     */
    private Map<String, Object> outputParams;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 执行结果消息
     */
    private String message;

    // ========== 以下是辅助字段，用于Mapper查询后解析 ==========

    /**
     * 原始命令内容(临时字段，用于解析)
     */
    @JsonIgnore
    private transient String rawCmdContent;

    /**
     * 执行结果提示(临时字段，用于解析)
     */
    @JsonIgnore
    private transient String resultTips;
}
