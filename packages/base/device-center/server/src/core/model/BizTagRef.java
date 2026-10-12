package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签与设备的应用关系
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("center_biz_tag_ref")
public class BizTagRef extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 设备ID
     */
    private Long businessKey;
}