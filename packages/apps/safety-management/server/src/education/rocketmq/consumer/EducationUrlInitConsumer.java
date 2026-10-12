package com.guhecloud.cube.srv.gen.safety.management.education.rocketmq.consumer;

import com.guhecloud.cube.srv.gen.safety.management.education.application.EducationMarketTaskUrlService;
import com.guhecloud.cube.srv.gen.safety.management.education.constant.EducationTaskTypeConstants;
import com.guhecloud.cube.srv.gen.safety.management.education.po.EducationMarketTaskUrlPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.rocketmq.message.MarketConfigMessage;
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
@RocketMQMessageListener(consumerGroup = "market_education_url_init", topic = MarketConfigMessage.TOPIC, selectorType = SelectorType.TAG)
public class EducationUrlInitConsumer implements RocketMQListener<MarketConfigMessage> {

    private final MarketConfigService marketConfigService;

    private final EducationMarketTaskUrlService educationMarketTaskUrlService;

    @Override
    public void onMessage(MarketConfigMessage message) {
        MarketConfigPO marketConfig = this.marketConfigService.getById(message.getMarketId());
        if (Objects.nonNull(marketConfig)) {
            EducationMarketTaskUrlPO educationMarketExamTaskUrlPO = new EducationMarketTaskUrlPO();
            educationMarketExamTaskUrlPO.setMarketTenant(marketConfig.getMarketTenant());
            educationMarketExamTaskUrlPO.setMarketId(marketConfig.getId());
            educationMarketExamTaskUrlPO.setTaskType(EducationTaskTypeConstants.EXAM_TASK);
            educationMarketExamTaskUrlPO.setEnabled(true);
            educationMarketTaskUrlService.save(educationMarketExamTaskUrlPO);
            EducationMarketTaskUrlPO educationMarketCultivateTaskUrlPO = new EducationMarketTaskUrlPO();
            educationMarketCultivateTaskUrlPO.setMarketTenant(marketConfig.getMarketTenant());
            educationMarketCultivateTaskUrlPO.setMarketId(marketConfig.getId());
            educationMarketCultivateTaskUrlPO.setTaskType(EducationTaskTypeConstants.CULTIVATE_TASK);
            educationMarketCultivateTaskUrlPO.setEnabled(true);
            educationMarketTaskUrlService.save(educationMarketCultivateTaskUrlPO);
        }
    }
}
