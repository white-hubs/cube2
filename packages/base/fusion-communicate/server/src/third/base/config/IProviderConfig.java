package com.guhecloud.cube.biz.fusion.communicate.server.third.base.config;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;

import java.util.List;

public interface IProviderConfig {

    ProviderConfig getProviderConfig(String key);

    void refreshProviderConfig(String key);

    List<ProviderConfig> getAllTenantProviderConfig(String key);
}
