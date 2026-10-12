package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertGlobalConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertGlobalConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;

/** 粮仓温度趋势预警全局默认配置。 */
public interface WarehouseTemperatureAlertGlobalConfigService extends IService<WarehouseTemperatureAlertGlobalConfig> {

    /** 查询全局默认配置。 */
    WarehouseTemperatureAlertGlobalConfigVO detail();

    /** 保存全局默认配置。 */
    Boolean saveConfig(WarehouseTemperatureAlertGlobalConfigSaveParam param);

    /** 获取当前生效配置。 */
    WarehouseTemperatureAlertGlobalConfigVO resolveEffectiveConfig();
}
