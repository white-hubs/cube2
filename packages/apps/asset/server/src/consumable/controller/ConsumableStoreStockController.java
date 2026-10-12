package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.property.management.client.property.vo.property.MoneyNumVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableInStoreParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStoreStockService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 仓库耗材库存
 *
 * @author Appet
 * @Description 仓库耗材库存
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableStoreStockController {
    private final ConsumableStoreStockService consumableStoreStockService;

    /**
     * 功能描述： 仓库耗材库存分页
     */
    @GetMapping(value = "/consumable-store-stock/page")
    public Pagination<ConsumableStoreStockExtendVO> getPage(@Valid ConsumableInStoreParam param) {
        return consumableStoreStockService.getPage(param);
    }

    /**
     * 统计即时库存总金额、数量
     */
    @GetMapping(value = "/consumable-store-stock/getTotalMoney")
    @DataPermissionSwitch(dataModel = "property:consumableList")
    public MoneyNumVO getTotalMoney(@RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "type", required = false) String type
                                   ) {
        return consumableStoreStockService.getTotalMoney(keyword, type);
    }

    /**
     * 功能描述： 仓库耗材库存详情
     */
    @GetMapping(value = "/consumable-store-stock/{id}")
    public ConsumableStoreStockVO getById(@PathVariable Long id) {
        return consumableStoreStockService.getById(id);
    }

}