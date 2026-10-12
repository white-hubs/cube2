package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

@Data
@TableName("meattrade_goods_weight_config")
public class GoodsWeightConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 商品类型主键
     */
    @Unique(composites = { "freshLabel", "deleted" }, message = "商品品类已存在")
    private String goodsCategoryId;

    /**
     * 商品类型名称
     */
    private String goodsCategoryName;

    /**
     * 标准重量
     */
    private Double standardWeight;

    /**
     * 重量单位
     */
    private String weightUnit;

    /**
     * 冷热鲜标识
     */
    @DiffLogField(name = "冷热鲜名称", function = "FRESH_LABEL_NAME")
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 备注信息
     */
    private String remark;
}
