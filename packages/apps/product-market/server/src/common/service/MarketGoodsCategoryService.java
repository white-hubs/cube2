package com.guhecloud.cube.srv.gen.product.market.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketGoodsCategory;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketGoodsCategoryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketGoodsCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketGoodsCategoryVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：商品品类服务层
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
public interface MarketGoodsCategoryService extends IService<MarketGoodsCategory> {

    Long add(MarketGoodsCategoryParam param);

    void update(Long id, MarketGoodsCategoryParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    MarketGoodsCategoryVO getById(Long id);

    Pagination<MarketGoodsCategoryVO> getPage(MarketGoodsCategoryQueryParam param);

    List<MarketGoodsCategoryVO> getListByIds(String ids);

}