package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlan;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingPlanVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：商品采价计划服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingPlanService extends IService<GoodsPricingPlan> {

    Long add(GoodsPricingPlanParam param);

    void update(Long id, GoodsPricingPlanParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPricingPlanVO getById(Long id);

    Pagination<GoodsPricingPlanVO> getPage(GoodsPricingPlanQueryParam param);

    void startOrStopPlan(Long id, boolean enabled);

    List<CommonStatVO> GoodsPricingPlanStatusStat();

    void removePlanJob(String jobId);
}