package com.guhecloud.cube.iot.mgt.base.model;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 命令记录详情表
 * iot_command_record
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "iot_command_record")
public class CommandRecord extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    private String instanceId;

    private String cmdSeq;

    private String rawCmdContent;

    private String translatedCmdContent;

    private String resultTips;

    private String singleCmdStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long deviceId;

    private String targetIdentifier;

    private static final long serialVersionUID = 1L;

    /**
     * 将设备属性设置命令转换为原始内容格式<br/>
     * 原始内容格式进行统一，方便各端解析，MGT、北向、Linkage等。
     *
     * @param deviceId 设备ID
     * @param propCode 属性Code
     * @param value    属性值
     *
     * @return 原始命令内容
     */
    public static String propSetCmdToRawContent(String deviceId, String propCode, Object value) {
        return String.format("{\"deviceId\":\"%s\",\"propCode\":\"%s\",\"value\":%s}", deviceId, propCode, value);
    }

    /**
     * 将设备方法调用命令转换为原始内容格式
     *
     * @param instanceId  命令实例ID
     * @param deviceId    设备ID
     * @param deviceName  设备名称
     * @param methodCode  方法编码
     * @param methodName  方法名称
     * @param methodType  方法类型
     * @param inputParams 输入参数
     * @return 原始命令内容
     */
    public static String methodInvokeCmdToRawContent(String instanceId, String deviceId, String deviceName,
                                                     String methodCode, String methodName, String methodType,
                                                     Map<String, Object> inputParams) {
        return JSON.toJSONString(Map.of(
                "instanceId", instanceId,
                "deviceId", deviceId,
                "deviceName", deviceName,
                "methodCode", methodCode,
                "methodName", methodName,
                "methodType", methodType,
                "inputParams", inputParams != null ? inputParams : Map.of()
        ));
    }
}