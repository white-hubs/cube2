package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseTemperatureDailyAvgMaxMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureDailyAvgMax;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureDailyAvgMaxService;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.SetUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 粮仓日平均最高温。
 */
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureDailyAvgMaxServiceImpl
        extends ServiceImpl<WarehouseTemperatureDailyAvgMaxMapper, WarehouseTemperatureDailyAvgMax>
        implements WarehouseTemperatureDailyAvgMaxService {

    @Override
    public void saveOrUpdateByWarehouseAndStatDate(List<WarehouseTemperatureDailyAvgMax> rows) {
        if (ListUtils.isEmpty(rows)) {
            return;
        }
        List<WarehouseTemperatureDailyAvgMax> normalizedRows = rows.stream()
                .filter(row -> row != null && row.getWarehouseId() != null && row.getStatDate() != null)
                .collect(java.util.stream.Collectors.toMap(
                        row -> buildKey(row.getWarehouseId(), row.getStatDate()),
                        row -> row,
                        (first, ignored) -> first,
                        LinkedHashMap::new))
                .values()
                .stream()
                .toList();
        if (ListUtils.isEmpty(normalizedRows)) {
            return;
        }
        Set<Long> warehouseIds = SetUtils.listToSet(normalizedRows, WarehouseTemperatureDailyAvgMax::getWarehouseId);
        Set<LocalDate> statDates = SetUtils.listToSet(normalizedRows, WarehouseTemperatureDailyAvgMax::getStatDate);
        List<WarehouseTemperatureDailyAvgMax> existingRows = list(Wrappers.<WarehouseTemperatureDailyAvgMax>lambdaQuery()
                                                                          .in(SetUtils.isNotEmpty(warehouseIds), WarehouseTemperatureDailyAvgMax::getWarehouseId, warehouseIds)
                                                                          .in(SetUtils.isNotEmpty(statDates), WarehouseTemperatureDailyAvgMax::getStatDate, statDates));
        Map<String, WarehouseTemperatureDailyAvgMax> existingByKey = new LinkedHashMap<>();
        for (WarehouseTemperatureDailyAvgMax existing : existingRows) {
            if (existing == null || existing.getWarehouseId() == null || existing.getStatDate() == null) {
                continue;
            }
            existingByKey.put(buildKey(existing.getWarehouseId(), existing.getStatDate()), existing);
        }
        List<WarehouseTemperatureDailyAvgMax> inserts = new ArrayList<>();
        List<WarehouseTemperatureDailyAvgMax> updates = new ArrayList<>();
        for (WarehouseTemperatureDailyAvgMax row : normalizedRows) {
            if (row == null || row.getWarehouseId() == null || row.getStatDate() == null) {
                continue;
            }
            WarehouseTemperatureDailyAvgMax existing = existingByKey.get(buildKey(row.getWarehouseId(), row.getStatDate()));
            if (existing == null) {
                row.setId(IdWorker.getId());
                inserts.add(row);
                continue;
            }
            row.setId(existing.getId());
            row.setDeleted(existing.getDeleted());
            row.setTenant(existing.getTenant());
            row.setCreateBy(existing.getCreateBy());
            row.setCreatorId(existing.getCreatorId());
            row.setCreatorType(existing.getCreatorType());
            row.setCreateTime(existing.getCreateTime());
            updates.add(row);
        }
        if (ListUtils.isNotEmpty(inserts)) {
            saveBatch(inserts);
        }
        if (ListUtils.isNotEmpty(updates)) {
            updateBatchById(updates);
        }
    }

    @Override
    public List<WarehouseTemperatureDailyAvgMax> listByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                                LocalDate beginDate,
                                                                                LocalDate endDate
                                                                               ) {
        if (ListUtils.isEmpty(warehouseIds) || beginDate == null || endDate == null) {
            return List.of();
        }
        return list(Wrappers.<WarehouseTemperatureDailyAvgMax>lambdaQuery()
                            .in(WarehouseTemperatureDailyAvgMax::getWarehouseId, warehouseIds)
                            .between(WarehouseTemperatureDailyAvgMax::getStatDate, beginDate, endDate)
                            .orderByAsc(WarehouseTemperatureDailyAvgMax::getWarehouseId)
                            .orderByAsc(WarehouseTemperatureDailyAvgMax::getStatDate));
    }

    @Override
    public Map<Long, List<WarehouseTemperatureDailyAvgMax>>
    mapByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                  LocalDate beginDate,
                                  LocalDate endDate
                                 ) {
        List<WarehouseTemperatureDailyAvgMax> rows = listByWarehouseIdsAndDateRange(warehouseIds, beginDate, endDate);
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        Map<Long, List<WarehouseTemperatureDailyAvgMax>> out = new LinkedHashMap<>();
        for (WarehouseTemperatureDailyAvgMax row : rows) {
            if (row == null || row.getWarehouseId() == null) {
                continue;
            }
            out.computeIfAbsent(row.getWarehouseId(), key -> new ArrayList<>()).add(row);
        }
        return out;
    }

    @Override
    public void markComplete(LocalDate statDate, LocalDateTime calculatedAt) {
        if (statDate == null) {
            return;
        }
        List<WarehouseTemperatureDailyAvgMax> rows = list(Wrappers.<WarehouseTemperatureDailyAvgMax>lambdaQuery()
                                                                  .eq(WarehouseTemperatureDailyAvgMax::getStatDate, statDate));
        if (ListUtils.isEmpty(rows)) {
            return;
        }
        for (WarehouseTemperatureDailyAvgMax row : rows) {
            row.setComplete(Boolean.TRUE);
            row.setCalculatedAt(calculatedAt);
        }
        updateBatchById(rows);
    }

    private static String buildKey(Long warehouseId, LocalDate statDate) {
        return warehouseId + "\u0000" + statDate;
    }
}
