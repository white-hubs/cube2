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
public class HomeApplicationCard extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 卡片主键
     */
    private Long cardId;

    /**
     * 卡片编号
     */
    @Unique(composites = { "deleted", "userId", "terminal" }, message = "已存在相同卡片")
    private String cardCode;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 卡片大小
     */
    private String cardSize;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 访问端编号
     */
    private String terminal;

}
