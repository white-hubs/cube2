package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.enumeration.MonthlyReportStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.event.MarketMonthlyReportWindUpEvent;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyWindUpPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyWindUpService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MonthlyReportService;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketMonthlyReportWindUpEventHandler {

    private final MonthlyReportService monthlyReportService;

    private final MarketMonthlyReportService marketMonthlyReportService;

    private final MarketMonthlyWindUpService marketMonthlyWindUpService;

    @EventListener
    public void monthlyReportSummary(final MarketMonthlyReportWindUpEvent event) {
        MonthlyReportPO lockMonthlyReportPO = this.monthlyReportService.getById(event.getMarketMonthlyReportPO().getMonthlyReportId());
        //使用分布式锁,避免被重复处理
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock(lockMonthlyReportPO.getId().toString(), Expiration.from(60000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                //存入上报副本
                this.marketMonthlyWindUpService.saveOrUpdate(event.getMarketMonthlyReportPO());
                MonthlyReportPO monthlyReportPO = this.monthlyReportService.getById(event.getMarketMonthlyReportPO().getMonthlyReportId());
                long marketMonthlyCount = this.marketMonthlyReportService.count(Wrappers.<MarketMonthlyReportPO>lambdaQuery()
                                                                                        .eq(MarketMonthlyReportPO::getMonthlyReportId, lockMonthlyReportPO.getId()));
                long windUpMarketTotal = this.marketMonthlyWindUpService.count(Wrappers.<MarketMonthlyWindUpPO>lambdaQuery()
                                                                                       .eq(MarketMonthlyWindUpPO::getMonthlyReportId, lockMonthlyReportPO.getId()));
                if (windUpMarketTotal >= marketMonthlyCount) {
                    monthlyReportPO.setStatus(MonthlyReportStatusEnum.COMPLETED.name());
                }
                this.monthlyReportService.updateById(monthlyReportPO);
            }
        } catch (Exception e) {
            log.error("市场月报上报失败,异常原因:{}", lockMonthlyReportPO.getId(), e);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }
}
