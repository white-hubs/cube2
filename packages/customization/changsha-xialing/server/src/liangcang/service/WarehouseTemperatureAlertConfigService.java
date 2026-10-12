package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertWarehouseConfigVO;

/** 粮仓温度趋势预警单仓配置。 */
public interface WarehouseTemperatureAlertConfigService extends IService<WarehouseTemperatureAlertConfig> {

    /** 查询单仓配置详情。 */
    WarehouseTemperatureAlertWarehouseConfigVO detail(Long warehouseId);

    /** 保存单仓配置。 */
    Boolean saveConfig(WarehouseTemperatureAlertWarehouseConfigSaveParam param);

    /** 重置单仓配置为全局默认。 */
    Boolean resetToGlobalDefault(Long warehouseId);

    /** 获取仓库当前生效配置。 */
    WarehouseTemperatureAlertGlobalConfigVO resolveEffectiveConfig(Long warehouseId);
}
