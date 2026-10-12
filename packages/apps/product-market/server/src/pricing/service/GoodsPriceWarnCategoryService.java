package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnCategoryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnCategoryVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价预警规则-适用类型服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPriceWarnCategoryService extends IService<GoodsPriceWarnCategory> {

    Long add(GoodsPriceWarnCategoryParam param);

    void update(Long id, GoodsPriceWarnCategoryParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPriceWarnCategoryVO getById(Long id);

    Pagination<GoodsPriceWarnCategoryVO> getPage(GoodsPriceWarnCategoryQueryParam param);

}