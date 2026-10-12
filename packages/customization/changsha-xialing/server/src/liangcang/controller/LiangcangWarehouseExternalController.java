package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehouseGoodsHistoryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehouseIdParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehousePeriodParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.LiangcangWarehouseExternalService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDailyStorageValueVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDailyTemperatureAverageGroupVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseGoodsFlowTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseGoodsHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseMeasureInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseDeviceInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 粮仓外部对接接口。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseExternalController {

    private final LiangcangWarehouseExternalService liangcangWarehouseExternalService;

    /**
     * 时间范围内绑定粮仓每天的温度均值；warehouseId 为空时查询所有绑定粮仓。
     */
    @PostMapping("/liangcang/external/warehouse/temperature/daily-average")
    public List<ExternalWarehouseDailyTemperatureAverageGroupVO> dailyTemperatureAverage(
            @RequestBody ExternalWarehousePeriodParam param) {
        return liangcangWarehouseExternalService.dailyTemperatureAverage(param);
    }

    /**
     * 时间范围内每天所有绑定粮仓的储量实时值。
     */
    @PostMapping("/liangcang/external/warehouse/storage/daily")
    public List<ExternalWarehouseDailyStorageValueVO> dailyStorageValue(@RequestBody ExternalWarehousePeriodParam param) {
        return liangcangWarehouseExternalService.dailyStorageValue(param);
    }

    /**
     * 某个绑定粮仓的详细信息。
     */
    @PostMapping("/liangcang/external/warehouse/detail")
    public ExternalWarehouseDetailVO warehouseDetail(@RequestBody ExternalWarehouseIdParam param) {
        return liangcangWarehouseExternalService.warehouseDetail(getWarehouseId(param));
    }

    /**
     * 某个绑定粮仓粮食出入库趋势。
     */
    @PostMapping("/liangcang/external/warehouse/goods-flow/trend")
    public List<ExternalWarehouseGoodsFlowTrendVO> goodsFlowTrend(@RequestBody ExternalWarehousePeriodParam param) {
        Long warehouseId = param == null ? null : param.getWarehouseId();
        return liangcangWarehouseExternalService.goodsFlowTrend(warehouseId, param);
    }

    /**
     * 粮仓货物历史记录；参数为空时查询全部。
     */
    @PostMapping("/liangcang/external/warehouse/goods-history/list")
    public List<ExternalWarehouseGoodsHistoryVO> goodsHistory(@RequestBody(required = false) ExternalWarehouseGoodsHistoryParam param) {
        return liangcangWarehouseExternalService.goodsHistory(param);
    }

    /**
     * 某个绑定粮仓的测量信息。
     */
    @PostMapping("/liangcang/external/warehouse/measure/info")
    public ExternalWarehouseMeasureInfoVO measureInfo(@RequestBody ExternalWarehouseIdParam param) {
        return liangcangWarehouseExternalService.measureInfo(getWarehouseId(param));
    }

    /**
     * 某个绑定粮仓的雷达信息。
     */
    @PostMapping("/liangcang/external/warehouse/radar/info")
    public List<WarehouseDeviceInfoVO> radarInfo(@RequestBody ExternalWarehouseIdParam param) {
        return liangcangWarehouseExternalService.radarInfo(getWarehouseId(param));
    }

    private Long getWarehouseId(ExternalWarehouseIdParam param) {
        return param == null ? null : param.getWarehouseId();
    }
}
