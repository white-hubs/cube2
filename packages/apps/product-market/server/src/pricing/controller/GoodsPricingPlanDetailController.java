package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingPlanDetailQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingPlanDetailService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingPlanDetailVO;
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
 * @Description 采价计划详情
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingPlanDetailController {
    private final GoodsPricingPlanDetailService goodsPricingPlanDetailService;

    /**
     * 功能描述： 采价计划详情添加
     */
    @PostMapping(value = "/goods-pricing-plan-detail")
    public Long add(@Valid @RequestBody GoodsPricingPlanDetailParam param) {
        return goodsPricingPlanDetailService.add(param);
    }

    /**
     * 功能描述： 采价计划详情修改
     */
    @PutMapping(value = "/goods-pricing-plan-detail/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPricingPlanDetailParam param) {
        goodsPricingPlanDetailService.update(id, param);
    }

    /**
     * 功能描述： 采价计划详情删除
     */
    @DeleteMapping(value = "/goods-pricing-plan-detail/{id}")
    public void delete(@PathVariable Long id) {
        goodsPricingPlanDetailService.delete(id);
    }

    /**
     * 功能描述： 采价计划详情批量删除
     */
    @DeleteMapping(value = "/goods-pricing-plan-detail/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPricingPlanDetailService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价计划详情分页
     */
    @GetMapping(value = "/goods-pricing-plan-detail/page")
    public Pagination<GoodsPricingPlanDetailVO> getPage(GoodsPricingPlanDetailQueryParam param) {
        return goodsPricingPlanDetailService.getPage(param);
    }

    /**
     * 功能描述： 采价计划详情详情
     */
    @GetMapping(value = "/goods-pricing-plan-detail/{id}")
    public GoodsPricingPlanDetailVO getById(@PathVariable Long id) {
        return goodsPricingPlanDetailService.getById(id);
    }

}