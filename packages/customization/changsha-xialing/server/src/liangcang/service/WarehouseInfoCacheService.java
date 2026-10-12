package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseInfoCache;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.LiangcangWarehousePageQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WarehouseInfoCacheService extends IService<WarehouseInfoCache> {

    List<LiangcangWarehousePageItemVO> listPageItems(LiangcangWarehousePageQueryParam param);

    Map<Long, LiangcangWarehousePageItemVO> listPageItemsByIds(Set<Long> warehouseIds);

    void upsertPageItems(List<LiangcangWarehousePageItemVO> items, Integer tenant);
}
