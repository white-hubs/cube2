package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRule;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleCheckVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价预警规则服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPriceWarnRuleService extends IService<GoodsPriceWarnRule> {

    Long add(GoodsPriceWarnRuleParam param);

    void update(Long id, GoodsPriceWarnRuleParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPriceWarnRuleVO getById(Long id);

    List<GoodsPriceWarnRuleVO> getByGroupId(Long groupId);

    Pagination<GoodsPriceWarnRuleVO> getPage(GoodsPriceWarnRuleQueryParam param);

    List<GoodsPriceWarnRuleCheckVO> checkWarnRule(GoodsPricingRecordParam pricingRecordParam);

}