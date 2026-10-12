package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class BaseMarketDataSyncProcesser<T> extends DataSyncProcesser<T> {

    private final MarketConfigService marketConfigService;

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            4,
            20,
            50,
            //时间单位
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("headMarketDataCollect-pool-%d").build(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMarketDataSyncProcesser.class);

    protected BaseMarketDataSyncProcesser(MarketConfigService marketConfigService) {
        Assert.isParameterError(Objects.isNull(marketConfigService),"marketConfigService must not be null");
        this.marketConfigService = marketConfigService;
    }

    @Override
    public ContextParam getContextParam(String param) {
        return marketConfigService.getContextParam(Long.parseLong(param));
    }

    public void marketDataSync() {
        SessionContextHolder.clearSessionContext();
        var marketDataConfigs = marketConfigService.list();
        for (var marketDataConfig : marketDataConfigs) {
            try {
                EXECUTOR.submit(()->dataSync(marketDataConfig.getId().toString()));
            }catch (Exception e){
                LOGGER.error("{} get market collect config error:",marketDataConfig.getId(),e);
            }
        }
    }

    public Map<String, String> getAuthorization(ContextParam param){
        Map<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "bearer " + param.getToken());
        authMap.put("tenant", param.getMarketTenant().toString());
        return authMap;
    }
}
