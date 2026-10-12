package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.guhecloud.cube.base.iot.product.model.v3.constant.ModelConstants;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3DictItemMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SystemDictCacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统字典缓存服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3SystemDictCacheServiceImpl implements V3SystemDictCacheService {

    private final V3DictItemMapper sysDictItemMapper;

    /**
     * dictId -> items 本地缓存（Guava）
     */
    private final Cache<Long, List<SysDictItem>> dictItemsCache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(24, TimeUnit.HOURS)
        .recordStats()
        .build();

    /**
     * 字典项ID -> Label 本地缓存（Guava）
     */
    private final Cache<Long, String> idToLabelCache = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(24, TimeUnit.HOURS)
        .recordStats()
        .build();

    @Override
    public List<SysDictItem> getDictItems(Long dictId) {
        if (dictId == null) {
            return Collections.emptyList();
        }

        List<SysDictItem> cached = dictItemsCache.getIfPresent(dictId);
        if (cached != null) {
            return cached;
        }

        List<SysDictItem> items = sysDictItemMapper.noTenantSelectByDictId(dictId);
        if (CollectionUtils.isEmpty(items)) {
            log.warn("系统字典查询结果为空，触发刷新重试，dictId={}", dictId);
            refreshCache(dictId);
            items = sysDictItemMapper.noTenantSelectByDictId(dictId);
        }

        List<SysDictItem> result = (items == null ? Collections.emptyList() : items);
        if (!CollectionUtils.isEmpty(result)) {
            dictItemsCache.put(dictId, result);
        }

        if (!CollectionUtils.isEmpty(result)) {
            for (SysDictItem item : result) {
                if (item != null && item.getId() != null && StringUtils.hasText(item.getItemLabel())) {
                    idToLabelCache.put(item.getId(), item.getItemLabel());
                }
            }
        }
        return result;
    }

    @Override
    public SysDictItem getDictItemByCode(Long dictId, String itemCode) {
        if (dictId == null || !StringUtils.hasText(itemCode)) {
            return null;
        }
        List<SysDictItem> items = getDictItems(dictId);
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        for (SysDictItem item : items) {
            if (item != null && itemCode.equals(item.getItemCode())) {
                if (item.getId() != null && StringUtils.hasText(item.getItemLabel())) {
                    idToLabelCache.put(item.getId(), item.getItemLabel());
                }
                return item;
            }
        }
        return null;
    }

    @Override
    public String getDictItemLabel(Long itemId) {
        if (itemId == null) {
            return null;
        }
        String cached = idToLabelCache.getIfPresent(itemId);
        if (StringUtils.hasText(cached)) {
            return cached;
        }

        SysDictItem item = sysDictItemMapper.noTenantSelectById(itemId);
        if (item == null || !StringUtils.hasText(item.getItemLabel())) {
            return null;
        }

        idToLabelCache.put(itemId, item.getItemLabel());
        return item.getItemLabel();
    }

    @Override
    public void refreshCache(Long dictId) {
        if (dictId != null) {
            dictItemsCache.invalidate(dictId);
        }
        idToLabelCache.invalidateAll();
        log.info("系统字典缓存已刷新，dictId={}", dictId);
    }
}
