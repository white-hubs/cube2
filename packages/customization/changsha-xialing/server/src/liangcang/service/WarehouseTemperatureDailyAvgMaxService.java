package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureDailyAvgMax;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/** 粮仓日平均最高温。 */
public interface WarehouseTemperatureDailyAvgMaxService extends IService<WarehouseTemperatureDailyAvgMax> {

    /** 批量幂等保存日均。 */
    void saveOrUpdateByWarehouseAndStatDate(List<WarehouseTemperatureDailyAvgMax> rows);

    /** 查询日期范围内日均。 */
    List<WarehouseTemperatureDailyAvgMax> listByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                         LocalDate beginDate,
                                                                         LocalDate endDate);

    /** 查询日期范围内日均并按仓分组。 */
    Map<Long, List<WarehouseTemperatureDailyAvgMax>> mapByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                                   LocalDate beginDate,
                                                                                   LocalDate endDate);

    /** 将指定日期标记为完整。 */
    void markComplete(LocalDate statDate, LocalDateTime calculatedAt);
}
