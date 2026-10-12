package com.guhecloud.kaihe.scada.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.kaihe.scada.server.entity.SysConfig;

/**
 * @program: gh-scada
 * @description: 配置信息
 * @author: hyq
 * @create: 2020-12-11 09:30
 **/
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 根据key获取value
     *
     * @param configKey
     * @return
     */
    public String getConfigValueByConfigKey(String configKey);

}
