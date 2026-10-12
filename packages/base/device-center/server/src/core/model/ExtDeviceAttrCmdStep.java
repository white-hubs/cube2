package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("center_ext_device_attr_cmd_step")
public class ExtDeviceAttrCmdStep extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 执行步骤
     */
    private String step;

    /**
     * 命令ID
     */
    private Long commandSeq;

    public static ExtDeviceAttrCmdStep create(Long commandSeq, String name) {
        ExtDeviceAttrCmdStep cmd = new ExtDeviceAttrCmdStep();
        cmd.setCommandSeq(commandSeq);
        cmd.setStep(name);
        return cmd;
    }
}