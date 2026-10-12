package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.MarketConfigMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketConfigQueryParam;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wql
 * @describe 市场配置
 * @date 2023/10/19 15:45
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketConfigServiceImpl extends ServiceImpl<MarketConfigMapper, MarketConfigPO> implements MarketConfigService {
    @Override
    public List<MarketConfigPO> getMarketConfigList(Integer marketTenant) {
        return this.lambdaQuery().eq(marketTenant != null, MarketConfigPO::getMarketTenant, marketTenant).list();
    }

    @Override
    public List<MarketConfigPO> getAllConfigs() {
        return getMarketConfigList(null);
    }

    @Override
    public List<MarketConfigPO> getConfigs(List<Integer> marketTenantIDs) {
        return this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(marketTenantIDs), MarketConfigPO::getMarketTenant, marketTenantIDs)
                .list();
    }

    @Override
    public List<MarketConfigPO> getList(MarketConfigQueryParam param) {
        return this.list(Wrappers.<MarketConfigPO>lambdaQuery()
                                 .like(StringUtils.isNotBlank(param.getKeyword()), MarketConfigPO::getMarketName, param.getKeyword())
                                 .eq(param.getMarketTenant() != null, MarketConfigPO::getMarketTenant, param.getMarketTenant())
                                 .in(CollectionUtils.isNotEmpty(param.getMarketTenantIds()), MarketConfigPO::getMarketTenant, param.getMarketTenantIds())
                                 .eq(MarketConfigPO::getStatus, 1));
    }
}
