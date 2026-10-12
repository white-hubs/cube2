package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRuleGroup;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleGroupParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleGroupQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleGroupPageVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleGroupVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价预警规则组服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPriceWarnRuleGroupService extends IService<GoodsPriceWarnRuleGroup> {

    Long add(GoodsPriceWarnRuleGroupParam param);

    void addWarnRuleGroup(GoodsPriceWarnRuleGroupParam param);

    void updateWarnRuleGroup(Long id, GoodsPriceWarnRuleGroupParam param);

    void updateStatus(Long id, Boolean status);

    GoodsPriceWarnRuleGroup update(Long id, GoodsPriceWarnRuleGroupParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPriceWarnRuleGroupVO getById(Long id);

    Pagination<GoodsPriceWarnRuleGroupPageVO> getPage(GoodsPriceWarnRuleGroupQueryParam param);

}