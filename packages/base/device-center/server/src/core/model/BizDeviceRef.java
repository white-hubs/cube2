package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备关联引用
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("center_biz_device_ref")
public class BizDeviceRef extends BasicEnhancedTenantEntity<Long> {
    private Long deviceId;

    private Long refDeviceId;

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }
}