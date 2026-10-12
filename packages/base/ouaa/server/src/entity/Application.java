package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 应用数据模型
 *
 * @author Woody
 * @date 2021/6/25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Application extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编号
     */
    @Unique(composites = { "deleted" }, message = "应用编号已存在")
    private String code;

    /**
     * 应用组主键
     */
    private Long applicationGroupId;

    /**
     * 应用名称
     */
    @Unique(composites = { "applicationGroupId", "deleted" }, message = "应用名称已存在")
    private String name;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 应用备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 最新版本
     */
    private String version;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 依赖应用集合
     */
    private String dependAppIds;

    /**
     * 是否公开访问
     */
    private Boolean publicAccess;
}
