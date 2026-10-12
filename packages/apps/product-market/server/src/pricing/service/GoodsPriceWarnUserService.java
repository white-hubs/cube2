package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnUser;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnUserParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnUserQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnUserVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价预警通知服务层
 *
 * @author Appet
 * @date 2023-06-05 15:08:36
 */
public interface GoodsPriceWarnUserService extends IService<GoodsPriceWarnUser> {

    Long add(GoodsPriceWarnUserParam param);

    void update(Long id, GoodsPriceWarnUserParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPriceWarnUserVO getById(Long id);

    Pagination<GoodsPriceWarnUserVO> getPage(GoodsPriceWarnUserQueryParam param);

}