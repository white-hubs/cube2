package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehouseGoodsHistoryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ExternalWarehousePeriodParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDailyTemperatureAverageGroupVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDailyStorageValueVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseGoodsFlowTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseGoodsHistoryVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ExternalWarehouseMeasureInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseDeviceInfoVO;

import java.util.List;

/** 粮仓外部对接查询。 */
public interface LiangcangWarehouseExternalService {

    List<ExternalWarehouseDailyTemperatureAverageGroupVO> dailyTemperatureAverage(ExternalWarehousePeriodParam param);

    List<ExternalWarehouseDailyStorageValueVO> dailyStorageValue(ExternalWarehousePeriodParam param);

    ExternalWarehouseDetailVO warehouseDetail(Long warehouseId);

    List<ExternalWarehouseGoodsFlowTrendVO> goodsFlowTrend(Long warehouseId, ExternalWarehousePeriodParam param);

    List<ExternalWarehouseGoodsHistoryVO> goodsHistory(ExternalWarehouseGoodsHistoryParam param);

    ExternalWarehouseMeasureInfoVO measureInfo(Long warehouseId);

    List<WarehouseDeviceInfoVO> radarInfo(Long warehouseId);
}
