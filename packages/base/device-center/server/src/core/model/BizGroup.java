package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分组
 *
 * @author lanchun
 * @date 2025/4/14 10:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("center_biz_group")
public class BizGroup extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;
}
