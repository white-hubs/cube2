package com.guhecloud.cube.base.iot.product.model.v3.service;

import com.guhecloud.cube.base.iot.product.model.v3.model.SysDictItem;

import java.util.List;

/**
 * 系统字典缓存服务
 */
public interface V3SystemDictCacheService {

    /**
     * 获取字典项列表（带缓存）
     *
     * @param dictId 字典ID
     * @return 字典项列表
     */
    List<SysDictItem> getDictItems(Long dictId);

    /**
     * 根据字典ID与项编码获取字典项（基于缓存数据过滤）
     *
     * @param dictId 字典ID
     * @param itemCode 字典项编码
     * @return 字典项，不存在返回 null
     */
    SysDictItem getDictItemByCode(Long dictId, String itemCode);

    /**
     * 根据字典项ID获取名称（优先走本地缓存）
     *
     * @param itemId 字典项ID
     * @return 名称，不存在返回 null
     */
    String getDictItemLabel(Long itemId);

    /**
     * 刷新指定字典缓存
     *
     * @param dictId 字典ID
     */
    void refreshCache(Long dictId);
}

