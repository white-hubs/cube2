package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseInfoCacheMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseInfoCache;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.LiangcangWarehousePageQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseInfoCacheService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseDeviceInfoVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.SetUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WarehouseInfoCacheServiceImpl extends ServiceImpl<WarehouseInfoCacheMapper, WarehouseInfoCache>
        implements WarehouseInfoCacheService {

    @Override
    public List<LiangcangWarehousePageItemVO> listPageItems(LiangcangWarehousePageQueryParam param) {
        var input = param != null && param.getInput() != null
                    ? param.getInput()
                    : new LiangcangWarehousePageQueryParam.WarehousePageInput();
        var query = Wrappers.<WarehouseInfoCache>lambdaQuery()
                .eq(WarehouseInfoCache::getDeleted, false)
                .orderByAsc(WarehouseInfoCache::getParentId)
                .orderByAsc(WarehouseInfoCache::getWarehouseId);
        if (input.getParentId() != null) {
            query.eq(WarehouseInfoCache::getParentId, input.getParentId());
        }
        if (StringUtils.isNotBlank(input.getSearch())) {
            query.like(WarehouseInfoCache::getName, input.getSearch().trim());
        }
        List<WarehouseInfoCache> rows = this.list(query);
        if (ListUtils.isEmpty(rows)) {
            return List.of();
        }
        return rows.stream()
                .map(WarehouseInfoCacheServiceImpl::toPageItem)
                .toList();
    }

    @Override
    public Map<Long, LiangcangWarehousePageItemVO> listPageItemsByIds(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        List<WarehouseInfoCache> rows = this.list(Wrappers.<WarehouseInfoCache>lambdaQuery()
                                                   .eq(WarehouseInfoCache::getDeleted, false)
                                                   .in(WarehouseInfoCache::getWarehouseId, warehouseIds));
        if (ListUtils.isEmpty(rows)) {
            return Map.of();
        }
        return rows.stream()
                .map(WarehouseInfoCacheServiceImpl::toPageItem)
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(LiangcangWarehousePageItemVO::getId, Function.identity(), (a, b) -> a));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upsertPageItems(List<LiangcangWarehousePageItemVO> items, Integer tenant) {
        if (ListUtils.isEmpty(items)) {
            return;
        }
        List<WarehouseInfoCache> rows = items.stream()
                .filter(item -> item != null && item.getId() != null)
                .collect(Collectors.toMap(
                        LiangcangWarehousePageItemVO::getId,
                        item -> toCache(item, tenant),
                        (a, b) -> a))
                .values()
                .stream()
                .sorted(Comparator.comparing(WarehouseInfoCache::getWarehouseId))
                .toList();
        if (ListUtils.nonEmpty(rows)) {
            saveOrUpdateByWarehouseIds(rows, tenant);
        }
    }

    private void saveOrUpdateByWarehouseIds(List<WarehouseInfoCache> rows, Integer tenant) {
        List<Long> warehouseIds = rows.stream()
                .map(WarehouseInfoCache::getWarehouseId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, WarehouseInfoCache> existingByWarehouseId = this.list(Wrappers.<WarehouseInfoCache>lambdaQuery()
                                                                      .eq(WarehouseInfoCache::getTenant, tenant)
                                                                      .eq(WarehouseInfoCache::getDeleted, false)
                                                                      .in(WarehouseInfoCache::getWarehouseId, warehouseIds))
                .stream()
                .collect(Collectors.toMap(WarehouseInfoCache::getWarehouseId, Function.identity(), (a, b) -> a));
        List<WarehouseInfoCache> toInsert = new ArrayList<>();
        List<WarehouseInfoCache> toUpdate = new ArrayList<>();
        for (WarehouseInfoCache row : rows) {
            WarehouseInfoCache existing = existingByWarehouseId.get(row.getWarehouseId());
            if (existing == null) {
                toInsert.add(row);
            } else {
                row.setId(existing.getId());
                toUpdate.add(row);
            }
        }
        if (ListUtils.nonEmpty(toUpdate)) {
            this.updateBatchById(toUpdate);
        }
        if (ListUtils.nonEmpty(toInsert)) {
            this.saveBatch(toInsert);
        }
    }

    private static WarehouseInfoCache toCache(LiangcangWarehousePageItemVO item, Integer tenant) {
        var row = new WarehouseInfoCache();
        row.setId(IdWorker.getId());
        row.setWarehouseId(item.getId());
        row.setParentId(item.getParentId());
        row.setName(item.getName());
        row.setCategoryName(item.getCategoryName());
        row.setTotalVolume(item.getTotalVolume());
        row.setQualityGrade(item.getQualityGrade());
        row.setWarehouseHeight(item.getWarehouseHeight());
        row.setEnabledScan(item.getEnabledScan());
        row.setScanCorn(item.getScanCorn());
        row.setMaterialVolume(item.getMaterialVolume());
        row.setMaterialWeight(item.getMaterialWeight());
        row.setUsageRate(item.getUsageRate());
        row.setCameraSetting(item.getCameraSetting());
        row.setSetting(item.getSetting());
        row.setDeviceListJson(JsonUtils.serialize(item.getDeviceList() == null ? List.of() : item.getDeviceList()));
        row.setDeleted(false);
        row.setTenant(tenant);
        return row;
    }

    private static LiangcangWarehousePageItemVO toPageItem(WarehouseInfoCache row) {
        var item = new LiangcangWarehousePageItemVO();
        item.setId(row.getWarehouseId());
        item.setParentId(row.getParentId());
        item.setName(row.getName());
        item.setCategoryName(row.getCategoryName());
        item.setTotalVolume(row.getTotalVolume());
        item.setQualityGrade(row.getQualityGrade());
        item.setWarehouseHeight(row.getWarehouseHeight());
        item.setEnabledScan(row.getEnabledScan());
        item.setScanCorn(row.getScanCorn());
        item.setMaterialVolume(row.getMaterialVolume());
        item.setMaterialWeight(row.getMaterialWeight());
        item.setUsageRate(row.getUsageRate());
        item.setCameraSetting(row.getCameraSetting());
        item.setSetting(row.getSetting());
        item.setDeviceList(parseDeviceList(row.getDeviceListJson()));
        return item;
    }

    private static List<WarehouseDeviceInfoVO> parseDeviceList(String json) {
        if (StringUtils.isBlank(json)) {
            return List.of();
        }
        try {
            List<WarehouseDeviceInfoVO> rows = JsonUtils.deserialize(json, new TypeReference<>() {
            });
            return ListUtils.isEmpty(rows) ? List.of() : rows;
        } catch (RuntimeException e) {
            return List.of();
        }
    }
}
