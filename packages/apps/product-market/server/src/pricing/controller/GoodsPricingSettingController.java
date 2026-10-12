package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingSettingParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingSettingService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingSettingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author Appet
 * @Description 采价相关设置
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingSettingController {
    private final GoodsPricingSettingService goodsPricingSettingService;

    /**
     * 功能描述： 添加
     */
    @PostMapping(value = "/goods-pricing-setting")
    public void add(@Valid @RequestBody GoodsPricingSettingParam param) {
        goodsPricingSettingService.addOrUpdate(param);
    }

    /**
     * 功能描述： 详情
     */
    @GetMapping(value = "/goods-pricing-setting/{code}")
    public GoodsPricingSettingVO getByCode(@PathVariable String code) {
        return goodsPricingSettingService.getByCode(code);
    }

}