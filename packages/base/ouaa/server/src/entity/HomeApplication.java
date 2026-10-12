package com.guhecloud.cube.biz.ouaa.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HomeApplication extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 应用编号
     */
    @Unique(composites = { "deleted", "userId", "terminal" }, message = "已存在相同应用")
    private String applicationCode;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 访问端编号
     */
    private String terminal;

}
