package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 风险指数权重分配
 * @date 2023/10/12 15:25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "weight_allot")
public class WeightAllotPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 指标类型
     */
    private String indexType;
    /**
     * 指标项
     */
    private String indexItem;
    /**
     * 权重占比
     */
    private Integer weight;

}
