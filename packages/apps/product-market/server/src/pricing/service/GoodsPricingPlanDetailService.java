package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlanDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingPlanDetailVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价计划详情服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingPlanDetailService extends IService<GoodsPricingPlanDetail> {

    Long add(GoodsPricingPlanDetailParam param);

    void update(Long id, GoodsPricingPlanDetailParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPricingPlanDetailVO getById(Long id);

    Pagination<GoodsPricingPlanDetailVO> getPage(GoodsPricingPlanDetailQueryParam param);

}