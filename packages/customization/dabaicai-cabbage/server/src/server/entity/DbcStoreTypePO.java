package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 大白菜挡位类型
 * 挡位类型同步过来是新增的，这个对象是记录同步过来的数据，并且记录在古河云新增的id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("dbc_store_type")
@EqualsAndHashCode(callSuper = true)
public class DbcStoreTypePO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 市场编号
     */
    private Integer marketId;
    //所属挡位类型序号
    private String storeTypeCode;
    //所属挡位类型名称
    private String storeTypeName;
    // 对应在古河云新增的挡位类型Id
    private Long stallCategoryId;
}
