package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 隐患单
 * @date 2023/9/11 16:40
 */
@Data
@TableName("safety_hidden_danger")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HiddenDangerPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 隐患单id
     */
    private Long id;
    /**
     * 隐患单名称
     */
    private String name;
    /**
     * 隐患分类
     */
    private String hiddenDangerCategory;
    /**
     * 隐患状态
     */
    private String hiddenDangerStatus;
    /**
     * 隐患级别
     */
    private String levelName;
    /**
     * 隐患描述
     */
    private String remark;
    /**
     * 市场租户
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
}
