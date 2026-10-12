package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseTemperatureAlertRecordMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertRecord;
import com.guhecloud.cube.customization.xialing.server.liangcang.enums.WarehouseTemperatureAlertStatus;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertRecordService;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/** 粮仓温度趋势预警记录。 */
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureAlertRecordServiceImpl
        extends ServiceImpl<WarehouseTemperatureAlertRecordMapper, WarehouseTemperatureAlertRecord>
        implements WarehouseTemperatureAlertRecordService {

    @Override
    public List<WarehouseTemperatureAlertRecord> listActiveByWarehouseIds(Collection<Long> warehouseIds) {
        if (ListUtils.isEmpty(warehouseIds)) {
            return List.of();
        }
        return list(Wrappers.<WarehouseTemperatureAlertRecord>lambdaQuery()
                .in(WarehouseTemperatureAlertRecord::getWarehouseId, warehouseIds)
                .eq(WarehouseTemperatureAlertRecord::getStatus, WarehouseTemperatureAlertStatus.ACTIVE.name())
                .orderByDesc(WarehouseTemperatureAlertRecord::getTriggeredAt));
    }

    @Override
    public Page<WarehouseTemperatureAlertRecord> pageByWarehouseIds(Collection<Long> warehouseIds,
                                                                     long pageNumber,
                                                                     long pageSize) {
        if (ListUtils.isEmpty(warehouseIds)) {
            return new Page<>(pageNumber, pageSize, 0L);
        }
        return page(new Page<>(pageNumber, pageSize),
                    Wrappers.<WarehouseTemperatureAlertRecord>lambdaQuery()
                            .in(WarehouseTemperatureAlertRecord::getWarehouseId, warehouseIds)
                            .orderByDesc(WarehouseTemperatureAlertRecord::getTriggeredAt));
    }

}
