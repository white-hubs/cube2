package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 下行命令表
 * iot_command_info
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "iot_command_info")
public class CommandInfo extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    private String cmdSeq;

    private String downCmd;

    private String translatedCmdContent;

    private String cmdFlag;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private static final long serialVersionUID = 1L;

    /**
     * 翻译设备属性设置命令内容
     *
     * @param deviceName 设备名称
     * @param propName   属性名称
     * @param value      属性值
     *
     * @return 翻译后的命令内容
     */
    public static String propSetCmdTranslatedContent(String deviceName, String propName, Object value) {
        return String.format("设备【%s.%s】写值为【 %s 】", deviceName, propName, value);
    }

    public static String devEventResetCmdTranslatedContent(String deviceName, String eventCode) {
        return String.format("设备【%s】重置事件【%s】", deviceName, eventCode);
    }

    public static String devPropResetCmdTranslatedContent(String deviceName, String propCode, Object resetValue) {
        return String.format("设备【%s】重置属性【%s】值为【%s】", deviceName, propCode, resetValue);
    }

    /**
     * 翻译设备方法调用命令内容
     *
     * @param deviceName 设备名称
     * @param methodName 方法名称
     * @return 翻译后的命令内容
     */
    public static String methodInvokeCmdTranslatedContent(String deviceName, String methodName) {
        return String.format("设备【%s】调用方法【%s】", deviceName, methodName);
    }
}