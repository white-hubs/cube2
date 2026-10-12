package com.guhecloud.cube.srv.gen.product.market.server.pricing.controller;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleGroupParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleGroupQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRuleGroupService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleGroupPageVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleGroupVO;
import com.guhecloud.platform.base.model.Pagination;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author Appet
 * @Description 采价预警规则组
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRuleGroupController {
    private final GoodsPriceWarnRuleGroupService goodsPriceWarnRuleGroupService;

    @PostMapping(value = "/goods-price-warn-rule-group")
    public void addGroup(@Valid @RequestBody GoodsPriceWarnRuleGroupParam param) {
        goodsPriceWarnRuleGroupService.addWarnRuleGroup(param);
    }

    /**
     * 功能描述： 采价预警规则组修改
     */
    @PutMapping(value = "/goods-price-warn-rule-group/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody GoodsPriceWarnRuleGroupParam param) {
        goodsPriceWarnRuleGroupService.updateWarnRuleGroup(id, param);
    }

    @PutMapping(value = "/goods-price-warn-rule-group/{id}/status/{status}")
    public void updateStatus(@PathVariable Long id, @PathVariable Boolean status) {
        goodsPriceWarnRuleGroupService.updateStatus(id, status);
    }

    /**
     * 功能描述： 采价预警规则组删除
     */
    @DeleteMapping(value = "/goods-price-warn-rule-group/{id}")
    public void delete(@PathVariable Long id) {
        goodsPriceWarnRuleGroupService.delete(id);
    }

    /**
     * 功能描述： 采价预警规则组批量删除
     */
    @DeleteMapping(value = "/goods-price-warn-rule-group/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        goodsPriceWarnRuleGroupService.batchDelete(ids);
    }

    /**
     * 功能描述： 采价预警规则组分页
     */
    @GetMapping(value = "/goods-price-warn-rule-group/page")
    public Pagination<GoodsPriceWarnRuleGroupPageVO> getPage(GoodsPriceWarnRuleGroupQueryParam param) {
        return goodsPriceWarnRuleGroupService.getPage(param);
    }

    /**
     * 功能描述： 采价预警规则组详情
     */
    @GetMapping(value = "/goods-price-warn-rule-group/{id}")
    public GoodsPriceWarnRuleGroupVO getById(@PathVariable Long id) {
        return goodsPriceWarnRuleGroupService.getById(id);
    }

}