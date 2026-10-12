package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 清理报废-资产;
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Data
@TableName("property_weed_out_relation")
public class PropertyWeedOutRelation extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 资产id
     */
    private Long propertyId;
    /**
     * 报废id
     */
    private Long weedOutId;
    /**
     * 报废单号
     */
    private String weedOutNumbers;
}
