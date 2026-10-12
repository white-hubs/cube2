package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("center_biz_group_ref")
public class BizGroupRef extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 设备id
     */
    private Long deviceId;
}
