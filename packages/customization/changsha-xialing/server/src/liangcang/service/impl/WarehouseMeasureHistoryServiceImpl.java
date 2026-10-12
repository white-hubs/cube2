package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseMeasureHistoryMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureAcquireKey;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseMeasureHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseMeasureHistoryService;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseMeasureHistoryServiceImpl extends ServiceImpl<WarehouseMeasureHistoryMapper, WarehouseMeasureHistory>
        implements WarehouseMeasureHistoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIgnoreDuplicates(List<WarehouseMeasureHistory> candidates) {
        if (ListUtils.isEmpty(candidates)) {
            return;
        }
        Map<WarehouseMeasureAcquireKey, WarehouseMeasureHistory> deduped = candidates.stream()
                .filter(h -> h.getWarehouseId() != null && h.getAcquisitionDate() != null)
                .collect(Collectors.toMap(
                        h -> new WarehouseMeasureAcquireKey(h.getWarehouseId(), h.getAcquisitionDate()),
                        h -> h,
                        (a, b) -> a));
        List<WarehouseMeasureAcquireKey> keys = deduped.keySet().stream().toList();
        if (ListUtils.isEmpty(keys)) {
            return;
        }
        List<WarehouseMeasureAcquireKey> existing = baseMapper.selectExistingKeys(keys);
        var existingSet = new HashSet<>(existing);
        List<WarehouseMeasureHistory> toInsert = deduped.values().stream()
                .filter(h -> !existingSet.contains(new WarehouseMeasureAcquireKey(h.getWarehouseId(), h.getAcquisitionDate())))
                .toList();
        if (ListUtils.nonEmpty(toInsert)) {
            saveBatch(toInsert);
        }
    }

    @Override
    public Optional<WarehouseMeasureHistory> latestByWarehouseId(Long warehouseId) {
        if (warehouseId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(latestByWarehouseIds(Set.of(warehouseId)).get(warehouseId));
    }

    @Override
    public Map<Long, WarehouseMeasureHistory> latestByWarehouseIds(Set<Long> warehouseIds) {
        if (warehouseIds == null || warehouseIds.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = warehouseIds.stream()
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        if (ListUtils.isEmpty(ids)) {
            return Map.of();
        }
        return baseMapper.selectLatestByWarehouseIds(ids).stream()
                .filter(row -> row.getWarehouseId() != null)
                .collect(Collectors.toMap(WarehouseMeasureHistory::getWarehouseId, row -> row, (a, b) -> a));
    }
}
