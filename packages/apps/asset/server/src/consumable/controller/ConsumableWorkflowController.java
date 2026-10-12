package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableAddStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockFlowParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableReduceStockParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理流引擎数据
 *
 * @author : lijie
 * @date : 2023-3-3
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/consumable")
public class ConsumableWorkflowController {
    private final ConsumableService consumableService;

    /**
     * 耗材入库
     */
    @PostMapping("/warehousing/submit")
    public Long warehousingSubmit(@RequestParam("prefix") String prefix) {
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "耗材信息有误");
        // 封装耗材入库基本信息
        var addRecordParam = JsonUtils.deserialize(prefix, ConsumableAddStockParam.class);
        Assert.isParameterError(addRecordParam == null, "耗材信息有误");
        // 封装耗材信息
        var dataObj = jsonObject.get("data");
        var array = JsonUtils.deserialize(dataObj, new TypeReference<List<ConsumableVO>>() {});
        if (CollectionUtils.isNotEmpty(array)) {
            List<ConsumableStockDetailParam> detailedList = new ArrayList<>();
            for (var detailed : array) {
                var param = new ConsumableStockDetailParam();
                param.setConsumableId(detailed.getId());
                param.setNum(detailed.getNum());
                param.setPrice(detailed.getPrice());
                param.setAmount(new BigDecimal(param.getNum()).multiply(param.getPrice()));
                detailedList.add(param);
            }
            addRecordParam.setDetails(detailedList);
        }
        return consumableService.addStock(addRecordParam);
    }

    /**
     * 耗材出库
     */
    @PostMapping("/outbound/submit")
    public Long outboundSubmit(@RequestParam("prefix") String prefix) {
        var jsonObject = JsonUtils.deserialize(prefix, new TypeReference<Map<String, Object>>() {});
        Assert.isParameterError(jsonObject == null, "耗材信息有误");
        // 封装耗材出库基本信息
        var reduceStock = JsonUtils.deserialize(prefix, ConsumableReduceStockFlowParam.class);
        Assert.isParameterError(reduceStock == null, "耗材信息有误");
        // 封装耗材信息
        var dataObj = jsonObject.get("data");
        var array = JsonUtils.deserialize(dataObj, new TypeReference<List<ConsumableVO>>() {});
        if (CollectionUtils.isNotEmpty(array)) {
            List<ConsumableStockDetailParam> detailedList = new ArrayList<>();
            for (var detailed : array) {
                var param = new ConsumableStockDetailParam();
                param.setConsumableId(detailed.getId());
                param.setNum(detailed.getNum());
                param.setPrice(detailed.getAvgPrice());
                param.setAmount(new BigDecimal(param.getNum()).multiply(param.getPrice()));
                detailedList.add(param);
            }
            reduceStock.setDetails(detailedList);
        }
        return consumableService.reduceStock(reduceStock.getConsumableReduceStockParam());
    }

    /**
     * 耗材出库提交-client接口
     */
    @PostMapping("/outbound/submit/client")
    public Long outboundSubmitClient(@RequestBody ConsumableReduceStockParam param) {
        param.setBusinessType(BusinessTypeEnum.APPROVE.name());
        return consumableService.reduceStock(param);
    }
}
