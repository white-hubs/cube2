package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 菜单数据模型
 *
 * @author Woody
 * @date 2021/6/30
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Menu extends BasicEnhancedTreeEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 访问端主键
     */
    private Long terminalId;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 分类字典主键
     */
    private Long categoryId;

    /**
     * 菜单编号
     */
    private String code;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 排序数字
     */
    private Float sort;

    /**
     * 菜单备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
