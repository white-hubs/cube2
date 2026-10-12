package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 联动预案数据模型
 *
 * @author lanchun
 * @date 2022/6/21 13:54
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Linkage extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 预案编号
     */
    private String code;

    /**
     * 预案名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
