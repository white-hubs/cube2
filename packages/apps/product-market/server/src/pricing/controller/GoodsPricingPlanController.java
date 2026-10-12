package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingPlanService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingPlanVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 商品采价计划
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingPlanController {
    private final GoodsPricingPlanService goodsPricingPlanService;

    /**
     * 功能描述： 商品采价计划添加
     */
    @PostMapping(value = "/goods-pricing-plan")
    public Long add(@Valid @RequestBody GoodsPricingPlanParam param) {
        return goodsPricingPlanService.add(param);
    }

    /**
     * 功能描述： 商品采价计划修改
     */
    @PutMapping(value = "/goods-pricing-plan/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPricingPlanParam param) {
        goodsPricingPlanService.update(id, param);
    }

    /**
     * 功能描述： 商品采价计划删除
     */
    @DeleteMapping(value = "/goods-pricing-plan/{id}")
    public void delete(@PathVariable Long id) {
        goodsPricingPlanService.delete(id);
    }

    /**
     * 功能描述： 商品采价计划批量删除
     */
    @DeleteMapping(value = "/goods-pricing-plan/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPricingPlanService.batchDelete(ids);
    }

    /**
     * 功能描述： 商品采价计划分页
     */
    @GetMapping(value = "/goods-pricing-plan/page")
    public Pagination<GoodsPricingPlanVO> getPage(GoodsPricingPlanQueryParam param) {
        return goodsPricingPlanService.getPage(param);
    }

    /**
     * 功能描述： 商品采价计划详情
     */
    @GetMapping(value = "/goods-pricing-plan/{id}")
    public GoodsPricingPlanVO getById(@PathVariable Long id) {
        return goodsPricingPlanService.getById(id);
    }

    /**
     * 开启或关闭计划
     *
     * @param id
     * @param enable
     */
    @PutMapping(value = "/goods-pricing-plan/{id}/enable/{enable}")
    public void startOrStopPlan(@PathVariable Long id, @PathVariable Boolean enable) {
        goodsPricingPlanService.startOrStopPlan(id, enable);
    }

    /**
     * 统计采价计划各种状态的数量
     *
     * @return
     */
    @GetMapping(value = "/goods-pricing-plan/status/stat")
    public List<CommonStatVO> GoodsPricingPlanStatusStat() {
        return goodsPricingPlanService.GoodsPricingPlanStatusStat();
    }

}