package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketConfigQueryParam;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;

import java.util.List;

/**
 * @author wql
 * @describe 市场配置
 * @date 2023/10/19 15:45
 */
public interface MarketConfigService extends IService<MarketConfigPO> {
    List<MarketConfigPO> getMarketConfigList(Integer marketTenant);

    List<MarketConfigPO> getAllConfigs();

    List<MarketConfigPO> getConfigs(List<Integer> marketTenantIDs);

    List<MarketConfigPO> getList(MarketConfigQueryParam configQueryParam);
}
