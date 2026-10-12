package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.init;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportUrlPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportUrlService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@EventBusListen(order = 1)
public class MonthlyReportUrlInitRunner {

    @Autowired
    private MarketConfigService marketConfigService;
    @Autowired
    private MarketMonthlyReportUrlService marketMonthlyReportUrlService;

    @Subscribe
    @AllowConcurrentEvents
    public void initMarketMonthlyReportUrl(SysReadEvent event) throws Exception {
        List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list();
        marketConfigPOS.stream().forEach(marketConfigPO -> {
            SessionContextHolder.requestTenant(marketConfigPO.getTenant());
            MarketMonthlyReportUrlPO marketMonthlyReportUrlServiceOne = this.marketMonthlyReportUrlService.getOne(Wrappers
                                                                                                                          .<MarketMonthlyReportUrlPO>lambdaQuery()
                                                                                                                          .eq(MarketMonthlyReportUrlPO::getMarketTenant, marketConfigPO.getMarketTenant())
                                                                                                                          .last("limit 1"));
            if (Objects.isNull(marketMonthlyReportUrlServiceOne)) {
                MarketMonthlyReportUrlPO marketMonthlyReportUrlPO = MarketMonthlyReportUrlPO.builder()
                        .marketId(marketConfigPO.getId())
                        .enabled(true)
                        .marketTenant(marketConfigPO.getMarketTenant())
                        .build();
                this.marketMonthlyReportUrlService.save(marketMonthlyReportUrlPO);
            }
            SessionContextHolder.clearSessionContext();
        });
    }
}
