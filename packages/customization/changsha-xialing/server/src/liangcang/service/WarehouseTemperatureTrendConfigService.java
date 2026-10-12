package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendConfigVO;

/** 仓库粮温趋势配置。 */
public interface WarehouseTemperatureTrendConfigService extends IService<WarehouseTemperatureTrendConfig> {

    /** 查询仓库粮温趋势配置详情。 */
    WarehouseTemperatureTrendConfigVO detail(Long warehouseId);

    /** 保存仓库粮温趋势配置。 */
    Boolean saveConfig(WarehouseTemperatureTrendConfigSaveParam param);
}
