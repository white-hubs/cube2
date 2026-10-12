package com.guhecloud.cube.srv.gen.product.market.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.client.dto.GoodsCategorySyncDTO;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：商品品类 Repository接口
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
@Repository
public interface MarketGoodsCategoryMapper extends BaseMapper<MarketGoodsCategory> {

    void insertOrUpdateBatch(List<GoodsCategorySyncDTO> dataList);
}