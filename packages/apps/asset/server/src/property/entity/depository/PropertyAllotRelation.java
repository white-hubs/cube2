package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 资产明细;
 *
 * @author : lijie
 * @date : 2023-3-15
 */
@Data
@TableName("property_allot_relation")
public class PropertyAllotRelation extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 资产id
     */
    private Long propertyId;
    /**
     * 出库id
     */
    private Long allotId;
    /**
     * 出库批号
     */
    private String allotNumbers;
}
