package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.vo.ConsumableStockVO;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;

import java.util.List;

public interface VisualizationConsumableService {
    List<PieChartValueVO> reduceRecord(StatisticalChartParam param);

    /**
     * 部门使用耗材占比
     *
     * @param param 查询时间
     *
     * @return 耗材出入库统计
     */
    List<PieChartValueVO> consumableByDep(StatisticalChartParam param);

    ConsumableStockVO stockAvailability();

}
