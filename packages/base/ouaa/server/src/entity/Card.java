package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 卡片数据模型
 *
 * @author lanchun
 * @date 2022/5/23 15:44
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ouaa_card")
public class Card extends BasicEnhancedEntity<Long> {

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
     * 应用编号
     */
    private String applicationCode;

    /**
     * 卡片编号
     */
    @Unique(composites = { "terminalId", "applicationId" }, message = "卡片编号已存在")
    private String code;

    /**
     * 卡片名称
     */
    @Unique(composites = { "terminalId", "applicationId" }, message = "卡片名称已存在")
    private String name;

    /**
     * 排序数字
     */
    private Integer sort;

    /**
     * 卡片备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否默认卡片
     */
    private Boolean defaultCard;
}
