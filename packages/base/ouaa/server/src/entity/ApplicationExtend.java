package com.guhecloud.cube.biz.ouaa.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 10:33
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ApplicationExtend extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 应用介绍
     */
    private String description;

    /**
     * 功能描述
     */
    private String featureDesc;

    /**
     * 使用帮助
     */
    private String helpInfo;

    /**
     * 购买说明
     */
    private String purchaseInfo;

    /**
     * 标题
     */
    private String title;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 描述
     */
    private String appDesc;
}
