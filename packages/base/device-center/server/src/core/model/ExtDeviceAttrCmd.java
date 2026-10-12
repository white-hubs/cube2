package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("center_ext_device_attr_cmd")
public class ExtDeviceAttrCmd extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 批量命令ID
     */
    private Long batchCmdId;

    /**
     * 命令ID
     */
    private Long commandSeq;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 属性code
     */
    private String attributeCode;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 执行结果
     */
    private String executeResult;

    /**
     * 原始值
     */
    private String originalValue;

    /**
     * 期望修改为的值
     */
    private String expectedValue;

    public static ExtDeviceAttrCmd create(Long batchCmdId, Long commandSeq, Long deviceId, String attrCode, String attrName,
                                          String attrValueExpected, String attrValueActual
                                         ) {
        ExtDeviceAttrCmd cmd = new ExtDeviceAttrCmd();
        cmd.setBatchCmdId(batchCmdId);
        cmd.setCommandSeq(commandSeq);
        cmd.setDeviceId(deviceId);
        cmd.setAttributeName(attrName);
        cmd.setAttributeCode(attrCode);
        cmd.setOriginalValue(attrValueActual);
        cmd.setExpectedValue(attrValueExpected);
        cmd.setExecuteResult("success");
        return cmd;
    }
}