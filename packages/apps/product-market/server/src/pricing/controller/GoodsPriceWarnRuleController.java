package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRuleService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleCheckVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleVO;
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
 * @Description 采价预警规则
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRuleController {
    private final GoodsPriceWarnRuleService goodsPriceWarnRuleService;

    /**
     * 功能描述： 采价预警规则添加
     */
    @PostMapping(value = "/goods-price-warn-rule")
    public Long add(@Valid @RequestBody GoodsPriceWarnRuleParam param) {
        return goodsPriceWarnRuleService.add(param);
    }

    /**
     * 功能描述： 采价预警规则修改
     */
    @PutMapping(value = "/goods-price-warn-rule/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPriceWarnRuleParam param) {
        goodsPriceWarnRuleService.update(id, param);
    }

    /**
     * 功能描述： 采价预警规则删除
     */
    @DeleteMapping(value = "/goods-price-warn-rule/{id}")
    public void delete(@PathVariable Long id) {
        goodsPriceWarnRuleService.delete(id);
    }

    /**
     * 功能描述： 采价预警规则批量删除
     */
    @DeleteMapping(value = "/goods-price-warn-rule/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPriceWarnRuleService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价预警规则分页
     */
    @GetMapping(value = "/goods-price-warn-rule/page")
    public Pagination<GoodsPriceWarnRuleVO> getPage(GoodsPriceWarnRuleQueryParam param) {
        return goodsPriceWarnRuleService.getPage(param);
    }

    /**
     * 功能描述： 采价预警规则详情
     */
    @GetMapping(value = "/goods-price-warn-rule/{id}")
    public GoodsPriceWarnRuleVO getById(@PathVariable Long id) {
        return goodsPriceWarnRuleService.getById(id);
    }

    /**
     * 功能描述： 采价预警检查
     */
    @PostMapping(value = "/goods-price-warn-rule/check")
    public List<GoodsPriceWarnRuleCheckVO> checkWarnRuleByGoodsCategoryId(@RequestBody GoodsPricingRecordParam pricingRecordParam) {
        return goodsPriceWarnRuleService.checkWarnRule(pricingRecordParam);
    }

}