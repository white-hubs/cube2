package com.guhecloud.cube.srv.gen.property.management.server.consumable.controller;

import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.vo.ConsumableStockVO;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.VisualizationConsumableService;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 耗材总览
 *
 * @author : lijie
 * @date : 2023-4-16
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/consumable/visualization")
public class VisualizationConsumableController {
    private final VisualizationConsumableService visualizationConsumableService;

    /**
     * 耗材出库趋势
     *
     * @param param 查询时间
     *
     * @return 耗材出入库统计
     */
    @GetMapping("/reduceRecord")
    public List<PieChartValueVO> reduceRecord(StatisticalChartParam param) {
        return visualizationConsumableService.reduceRecord(param);
    }

    /**
     * 部门使用耗材占比
     *
     * @param param 查询时间
     *
     * @return 耗材出入库统计
     */
    @GetMapping("/consumableByDep")
    public List<PieChartValueVO> consumableByDep(StatisticalChartParam param) {
        return visualizationConsumableService.consumableByDep(param);
    }

    /**
     * 查询耗材库存情况
     *
     * @return 耗材库存情况
     */
    @GetMapping("/stockAvailability")
    @DataPermissionSwitch(dataModel = "property:consumableList")
    public ConsumableStockVO stockAvailability() {
        return visualizationConsumableService.stockAvailability();
    }
}
