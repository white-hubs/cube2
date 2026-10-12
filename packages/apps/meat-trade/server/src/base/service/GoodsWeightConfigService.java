package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.GoodsWeightConfig;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;

import java.util.List;

public interface GoodsWeightConfigService extends IService<GoodsWeightConfig> {

    Long saveGoodsWeightConfig(GoodsWeightConfig goodsWeightConfig);

    void update(Long id, GoodsWeightConfig oldConfig, GoodsWeightConfig newConfig);

    GoodsWeightConfig getGoodWeightConfig(String goodsCategoryId, MeatGoodsFreshLabelEnum freshLabel);

    List<GoodsWeightConfig> listGoodWeightConfig();

    void remove(GoodsWeightConfig goodsWeightConfig);
}
