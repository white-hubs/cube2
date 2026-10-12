package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data
@TableName("center_biz_tag_rule")
public class BizTagRule extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 标签编号
     */
    private String tagCode;

    /**
     * 规则内容
     */
    private String rule;
}
