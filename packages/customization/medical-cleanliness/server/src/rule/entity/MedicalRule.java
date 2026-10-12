package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * ;
 *
 * @author : lijie
 * @date : 2024-3-20
 */
@TableName("medical_rule")
@Data
public class MedicalRule extends BasicEnhancedTenantEntity<Long> {
    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 产品id
     */
    private String productId;
    /**
     *
     */
    private String deviceIds;
    /**
     * 下发联动规则
     */
    private Long iotRuleId;
    /**
     * 指标区域
     */
    private String areaOptions;
}