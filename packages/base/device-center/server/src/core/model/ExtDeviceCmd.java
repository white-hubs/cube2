package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("center_ext_device_cmd")
public class ExtDeviceCmd extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 批量命令ID
     */
    private Long batchCmdSeq;

    public static ExtDeviceCmd create(Long batchCmdSeq) {
        ExtDeviceCmd cmd = new ExtDeviceCmd();
        cmd.setBatchCmdSeq(batchCmdSeq);
        return cmd;
    }
}