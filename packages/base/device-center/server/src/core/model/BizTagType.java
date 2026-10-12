package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 标签类别
 */
@Data
@TableName("center_biz_tag_type")
public class BizTagType extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 标签类型
     */
    private String tagTypeCode;

    /**
     * 标签名称
     */
    private String tagTypeName;
}