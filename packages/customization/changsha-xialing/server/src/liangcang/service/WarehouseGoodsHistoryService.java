package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseGoodsHistory;

import java.util.List;

public interface WarehouseGoodsHistoryService extends IService<WarehouseGoodsHistory> {

    /** 按 仓+采集时间 去重后插入。 */
    void saveIgnoreDuplicates(List<WarehouseGoodsHistory> candidates);
}
