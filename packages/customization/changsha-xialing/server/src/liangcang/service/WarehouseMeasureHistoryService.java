package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface WarehouseMeasureHistoryService extends IService<WarehouseMeasureHistory> {

    /** 按仓库和采集时间去重后插入。 */
    void saveIgnoreDuplicates(List<WarehouseMeasureHistory> candidates);

    /** 查询仓库最新测量信息。 */
    Optional<WarehouseMeasureHistory> latestByWarehouseId(Long warehouseId);

    /** 批量查询仓库最新测量信息。 */
    Map<Long, WarehouseMeasureHistory> latestByWarehouseIds(Set<Long> warehouseIds);
}
