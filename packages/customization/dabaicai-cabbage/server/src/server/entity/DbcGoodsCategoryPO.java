package com.guhecloud.cube.srv.gen.cabbage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 大白菜商品品类表
 */
@Data
@TableName("dbc_goods_category")
@EqualsAndHashCode(callSuper = true)
public class DbcGoodsCategoryPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * //市场序号
     */
    private Integer marketId;
    /**
     * 品类序号
     */
    private String oid;
    /**
     * 品类编号
     */
    private String code;
    /**
     * 品类名称
     */
    private String name;
    /**
     * 上级序号
     */
    private String parent;
    /**
     * 等级
     */
    private String lev;
}
