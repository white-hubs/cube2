package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseGoodsHistoryMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseGoodsAcquireKey;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseGoodsHistory;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseGoodsHistoryService;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseGoodsHistoryServiceImpl extends ServiceImpl<WarehouseGoodsHistoryMapper, WarehouseGoodsHistory>
        implements WarehouseGoodsHistoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIgnoreDuplicates(List<WarehouseGoodsHistory> candidates) {
        if (ListUtils.isEmpty(candidates)) {
            return;
        }
        Map<WarehouseGoodsAcquireKey, WarehouseGoodsHistory> deduped = candidates.stream()
                .filter(h -> h.getWarehouseId() != null && h.getAcquisitionDate() != null)
                .collect(Collectors.toMap(
                        h -> new WarehouseGoodsAcquireKey(h.getWarehouseId(), h.getAcquisitionDate()),
                        h -> h,
                        (a, b) -> a));
        List<WarehouseGoodsAcquireKey> keys = deduped.keySet().stream().toList();
        if (ListUtils.isEmpty(keys)) {
            return;
        }
        List<WarehouseGoodsAcquireKey> existing = baseMapper.selectExistingKeys(keys);
        var existingSet = new HashSet<>(existing);
        List<WarehouseGoodsHistory> toInsert = deduped.values().stream()
                .filter(h -> !existingSet.contains(new WarehouseGoodsAcquireKey(h.getWarehouseId(), h.getAcquisitionDate())))
                .toList();
        if (ListUtils.nonEmpty(toInsert)) {
            this.saveBatch(toInsert);
        }
    }
}
