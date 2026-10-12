package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertRecord;

import java.util.Collection;
import java.util.List;

/** 粮仓温度趋势预警记录。 */
public interface WarehouseTemperatureAlertRecordService extends IService<WarehouseTemperatureAlertRecord> {

    /** 按仓查询激活记录。 */
    List<WarehouseTemperatureAlertRecord> listActiveByWarehouseIds(Collection<Long> warehouseIds);

    /** 按仓分页查询预警记录，按触发时间倒序。 */
    Page<WarehouseTemperatureAlertRecord> pageByWarehouseIds(Collection<Long> warehouseIds,
                                                              long pageNumber,
                                                              long pageSize);

}
