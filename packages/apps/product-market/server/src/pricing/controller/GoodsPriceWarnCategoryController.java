package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnCategoryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnCategoryQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnCategoryService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnCategoryVO;
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
 * @Description 采价预警规则-适用类型
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnCategoryController {
    private final GoodsPriceWarnCategoryService goodsPriceWarnCategoryService;

    /**
     * 功能描述： 采价预警规则-适用类型添加
     */
    @PostMapping(value = "/goods-price-warn-category")
    public Long add(@Valid @RequestBody GoodsPriceWarnCategoryParam param) {
        return goodsPriceWarnCategoryService.add(param);
    }

    /**
     * 功能描述： 采价预警规则-适用类型修改
     */
    @PutMapping(value = "/goods-price-warn-category/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPriceWarnCategoryParam param) {
        goodsPriceWarnCategoryService.update(id, param);
    }

    /**
     * 功能描述： 采价预警规则-适用类型删除
     */
    @DeleteMapping(value = "/goods-price-warn-category/{id}")
    public void delete(@PathVariable Long id) {
        goodsPriceWarnCategoryService.delete(id);
    }

    /**
     * 功能描述： 采价预警规则-适用类型批量删除
     */
    @DeleteMapping(value = "/goods-price-warn-category/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPriceWarnCategoryService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价预警规则-适用类型分页
     */
    @GetMapping(value = "/goods-price-warn-category/page")
    public Pagination<GoodsPriceWarnCategoryVO> getPage(GoodsPriceWarnCategoryQueryParam param) {
        return goodsPriceWarnCategoryService.getPage(param);
    }

    /**
     * 功能描述： 采价预警规则-适用类型详情
     */
    @GetMapping(value = "/goods-price-warn-category/{id}")
    public GoodsPriceWarnCategoryVO getById(@PathVariable Long id) {
        return goodsPriceWarnCategoryService.getById(id);
    }

}