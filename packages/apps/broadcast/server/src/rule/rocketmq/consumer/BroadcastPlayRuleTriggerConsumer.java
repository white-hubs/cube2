package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.rocketmq.consumer;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.event.BroadcastPlayTextEvent;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.response.BroadcastDeviceResponse;
import com.guhecloud.cube.biz.subsystem.broadcast.server.common.infrastruceture.facade.securitymanage.service.BroadcastDeviceQueryService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.rocketmq.message.BroadcastPlayRuleTriggerMessage;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleDeviceVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = BroadcastPlayRuleTriggerMessage.TOPIC + "_" + "BROADCAST",
                         topic = BroadcastPlayRuleTriggerMessage.TOPIC
)
public class BroadcastPlayRuleTriggerConsumer implements RocketMQListener<BroadcastPlayRuleTriggerMessage> {

    private final BroadcastPlayRuleService broadcastPlayRuleService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final BroadcastDeviceQueryService broadcastDeviceQueryService;

    @Override
    public void onMessage(BroadcastPlayRuleTriggerMessage message) {
        log.debug("broadcast_rule_trigger,trigger rule id:{}", message.getRuleIds());
        for (Long ruleId : message.getRuleIds()) {
            BroadcastPlayRuleVO playRuleServiceDetail = this.broadcastPlayRuleService.getDetail(ruleId);
            if (Objects.isNull(playRuleServiceDetail)) {
                log.error("broadcast_rule_trigger fail,rule id is null,roleId:{}", ruleId);
            }
            if (!playRuleServiceDetail.getEnabled()) {
                log.debug("broadcast_rule_trigger ignore, trigger rule enabled is false id:{}", message.getRuleIds());
            }
            if (CollectionUtils.isEmpty(playRuleServiceDetail.getDevices())) {
                log.debug("broadcast_rule_trigger ignore, trigger rule devices is empty id:{}", message.getRuleIds());
            }
            if (CollectionUtils.isEmpty(playRuleServiceDetail.getTemplates())) {
                log.debug("broadcast_rule_trigger ignore, trigger rule templates is empty id:{}", message.getRuleIds());
            }
            List<Long> deviceIds = playRuleServiceDetail.getDevices().stream().map(BroadcastPlayRuleDeviceVO::getDeviceId)
                    .collect(Collectors.toList());
            List<BroadcastDeviceResponse> broadcastDeviceResponses = this.broadcastDeviceQueryService.queryBroadcastDevicesByIds(deviceIds);
            //获取设备第三方id
            Set<String> deviceThirdPartyIds = broadcastDeviceResponses.stream()
                    .map(BroadcastDeviceResponse::getThirdPartyId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());
            playRuleServiceDetail.getTemplates().forEach(template -> {
                //发送广播播放事件
                BroadcastPlayTextEvent broadcastPlayTextEvent = new BroadcastPlayTextEvent(this,
                                                                                           template.getTemplateContent(),
                                                                                           deviceThirdPartyIds,
                                                                                           1
                );
                this.applicationEventPublisher.publishEvent(broadcastPlayTextEvent);
            });
        }
    }
}
