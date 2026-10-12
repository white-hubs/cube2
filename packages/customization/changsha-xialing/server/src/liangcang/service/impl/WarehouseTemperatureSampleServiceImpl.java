package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseTemperatureSampleMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureSample;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureSampleService;
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

/** 粮仓温度趋势采样。 */
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureSampleServiceImpl
        extends ServiceImpl<WarehouseTemperatureSampleMapper, WarehouseTemperatureSample>
        implements WarehouseTemperatureSampleService {

    @Override
    public void saveOrUpdateByWarehouseAndSampleTime(List<WarehouseTemperatureSample> rows) {
        if (ListUtils.isEmpty(rows)) {
            return;
        }
        List<WarehouseTemperatureSample> normalizedRows = rows.stream()
                .filter(row -> row != null && row.getWarehouseId() != null && row.getSampleTime() != null)
                .collect(java.util.stream.Collectors.toMap(
                        row -> buildKey(row.getWarehouseId(), row.getSampleTime()),
                        row -> row,
                        (first, ignored) -> first,
                        LinkedHashMap::new))
                .values()
                .stream()
                .toList();
        if (ListUtils.isEmpty(normalizedRows)) {
            return;
        }
        Set<Long> warehouseIds = SetUtils.listToSet(normalizedRows, WarehouseTemperatureSample::getWarehouseId);
        Set<LocalDateTime> sampleTimes = SetUtils.listToSet(normalizedRows, WarehouseTemperatureSample::getSampleTime);
        List<WarehouseTemperatureSample> existingRows = list(Wrappers.<WarehouseTemperatureSample>lambdaQuery()
                .in(SetUtils.isNotEmpty(warehouseIds), WarehouseTemperatureSample::getWarehouseId, warehouseIds)
                .in(SetUtils.isNotEmpty(sampleTimes), WarehouseTemperatureSample::getSampleTime, sampleTimes));
        Map<String, WarehouseTemperatureSample> existingByKey = new LinkedHashMap<>();
        for (WarehouseTemperatureSample existing : existingRows) {
            if (existing == null || existing.getWarehouseId() == null || existing.getSampleTime() == null) {
                continue;
            }
            existingByKey.put(buildKey(existing.getWarehouseId(), existing.getSampleTime()), existing);
        }
        List<WarehouseTemperatureSample> inserts = new ArrayList<>();
        for (WarehouseTemperatureSample row : normalizedRows) {
            if (row == null || row.getWarehouseId() == null || row.getSampleTime() == null) {
                continue;
            }
            WarehouseTemperatureSample existing = existingByKey.get(buildKey(row.getWarehouseId(), row.getSampleTime()));
            if (existing == null) {
                row.setId(IdWorker.getId());
                inserts.add(row);
            }
        }
        if (ListUtils.isNotEmpty(inserts)) {
            saveBatch(inserts);
        }
    }

    @Override
    public List<WarehouseTemperatureSample> listByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                           LocalDate beginDate,
                                                                           LocalDate endDate) {
        if (ListUtils.isEmpty(warehouseIds) || beginDate == null || endDate == null) {
            return List.of();
        }
        LocalDateTime beginTime = beginDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1L).atStartOfDay();
        return listByWarehouseIdsAndTimeRange(warehouseIds, beginTime, endTime);
    }

    @Override
    public Map<Long, List<WarehouseTemperatureSample>> mapByWarehouseIdsAndDateRange(Collection<Long> warehouseIds,
                                                                                     LocalDate beginDate,
                                                                                     LocalDate endDate) {
        List<WarehouseTemperatureSample> rows = listByWarehouseIdsAndDateRange(warehouseIds, beginDate, endDate);
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        Map<Long, List<WarehouseTemperatureSample>> out = new LinkedHashMap<>();
        for (WarehouseTemperatureSample row : rows) {
            if (row == null || row.getWarehouseId() == null) {
                continue;
            }
            out.computeIfAbsent(row.getWarehouseId(), key -> new ArrayList<>()).add(row);
        }
        return out;
    }

    @Override
    public List<WarehouseTemperatureSample> listByWarehouseIdsAndTimeRange(Collection<Long> warehouseIds,
                                                                           LocalDateTime beginTime,
                                                                           LocalDateTime endTime) {
        if (ListUtils.isEmpty(warehouseIds) || beginTime == null || endTime == null) {
            return List.of();
        }
        return list(Wrappers.<WarehouseTemperatureSample>lambdaQuery()
                .in(WarehouseTemperatureSample::getWarehouseId, warehouseIds)
                .between(WarehouseTemperatureSample::getSampleTime, beginTime, endTime)
                .orderByAsc(WarehouseTemperatureSample::getWarehouseId)
                .orderByAsc(WarehouseTemperatureSample::getSampleTime));
    }

    private static String buildKey(Long warehouseId, LocalDateTime sampleTime) {
        return warehouseId + "\u0000" + sampleTime;
    }
}
