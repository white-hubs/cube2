package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.entity.SysConfig;
import com.guhecloud.kaihe.scada.server.mapper.SysConfigMapper;
import com.guhecloud.kaihe.scada.server.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: gh-scada
 * @description: 系统配置文件实现类
 * @author: hyq
 * @create: 2020-12-11 09:37
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {
    private final SysConfigMapper configMapper;

    /**
     * 根据key获取value
     *
     * @param configKey
     * @return
     */
    @Override
    public String getConfigValueByConfigKey(String configKey) {
        SysConfig config = configMapper.selectOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfigKey, configKey));
        return config == null ? "" : config.getConfigValue();
    }
}
