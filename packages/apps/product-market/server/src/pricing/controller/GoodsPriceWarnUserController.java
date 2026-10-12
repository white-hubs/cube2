package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnUserParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnUserQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnUserService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnUserVO;
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
 * @Description 采价预警通知
 * @date 2023-06-05 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnUserController {
    private final GoodsPriceWarnUserService goodsPriceWarnUserService;

    /**
     * 功能描述： 采价预警通知添加
     */
    @PostMapping(value = "/goods-price-warn-user")
    public Long add(@Valid @RequestBody GoodsPriceWarnUserParam param) {
        return goodsPriceWarnUserService.add(param);
    }

    /**
     * 功能描述： 采价预警通知修改
     */
    @PutMapping(value = "/goods-price-warn-user/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPriceWarnUserParam param) {
        goodsPriceWarnUserService.update(id, param);
    }

    /**
     * 功能描述： 采价预警通知删除
     */
    @DeleteMapping(value = "/goods-price-warn-user/{id}")
    public void delete(@PathVariable Long id) {
        goodsPriceWarnUserService.delete(id);
    }

    /**
     * 功能描述： 采价预警通知批量删除
     */
    @DeleteMapping(value = "/goods-price-warn-user/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPriceWarnUserService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价预警通知分页
     */
    @GetMapping(value = "/goods-price-warn-user/page")
    public Pagination<GoodsPriceWarnUserVO> getPage(GoodsPriceWarnUserQueryParam param) {
        return goodsPriceWarnUserService.getPage(param);
    }

    /**
     * 功能描述： 采价预警通知详情
     */
    @GetMapping(value = "/goods-price-warn-user/{id}")
    public GoodsPriceWarnUserVO getById(@PathVariable Long id) {
        return goodsPriceWarnUserService.getById(id);
    }

}