package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeConfigParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigExtendVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigVO;

/**
 * 描述：区域分项配置服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface EnergyChargeConfigService extends IService<EnergyChargeConfig> {

    void add(EnergyChargeConfigParam param);

    EnergyChargeConfigVO getByType(String instrumentType, String chargeType);

    /**
     * 获取有效的配置
     */
    EnergyChargeConfigExtendVO getValidConfig();

}