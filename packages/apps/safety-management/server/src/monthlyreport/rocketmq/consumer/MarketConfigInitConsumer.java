package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.MarketMonthlyReportUrlModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.rocketmq.message.MarketConfigMessage;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportUrlService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "market_monthly_report", topic = MarketConfigMessage.TOPIC, selectorType = SelectorType.TAG)
public class MarketConfigInitConsumer implements RocketMQListener<MarketConfigMessage> {

    private final MarketConfigService marketConfigService;

    private final MarketMonthlyReportUrlService marketMonthlyReportUrlService;

    @Override
    public void onMessage(MarketConfigMessage message) {
        MarketConfigPO marketConfig = this.marketConfigService.getById(message.getMarketId());
        if (Objects.nonNull(marketConfig)) {
            MarketMonthlyReportUrlModifyRequest param = new MarketMonthlyReportUrlModifyRequest();
            param.setMarketTenant(marketConfig.getMarketTenant());
            param.setMarketId(marketConfig.getId());
            marketMonthlyReportUrlService.save(param);
        }
    }
}
