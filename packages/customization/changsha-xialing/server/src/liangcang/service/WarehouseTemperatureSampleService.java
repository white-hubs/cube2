package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureSample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/** 粮仓温度趋势采样。 */
public interface WarehouseTemperatureSampleService extends IService<WarehouseTemperatureSample> {

    /** 批量保存采样；同仓同采样时间已存在时跳过。 */
    void saveOrUpdateByWarehouseAndSampleTime(List<WarehouseTemperatureSample> rows);

    /** 查询日期范围内采样。 */
    List<WarehouseTemperatureSample> listByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                     LocalDate beginDate,
                                                                     LocalDate endDate);

    /** 查询日期范围内采样并按仓分组。 */
    Map<Long, List<WarehouseTemperatureSample>> mapByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                              LocalDate beginDate,
                                                                              LocalDate endDate);

    /** 查询仓库时间区间采样。 */
    List<WarehouseTemperatureSample> listByWarehouseIdsAndTimeRange(Collection<Long> warehouseIds,
                                                                    LocalDateTime beginTime,
                                                                    LocalDateTime endTime);
}
