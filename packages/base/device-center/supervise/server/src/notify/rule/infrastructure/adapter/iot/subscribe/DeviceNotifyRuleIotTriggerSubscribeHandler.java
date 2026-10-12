package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.subscribe;

import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.DeviceNotifyRuleTriggerEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.adapter.iot.subscribe.parser.RuleTriggerSnapshotParser;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.DeviceNotifyRuleRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceNotifyRuleIotTriggerSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final DeviceCenterStdClient deviceCenterStdClient;

    private final DeviceNotifyRuleRepository deviceNotifyRuleRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RuleTriggerSnapshotParser ruleTriggerSnapshotParser;

    @EventListener
    public void subscribeRuleTrigger(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerRuleTriggerEventMsgHandler((ruleTriggerMsg) -> {
            SessionContextHolder.clearSessionContext();
            //获取规则配置
            var deviceNotifyRule = deviceNotifyRuleRepository.findByIotId(Long.valueOf(ruleTriggerMsg.getRuleId()));
            if (Objects.isNull(deviceNotifyRule)) {
                log.info("IoT rule triggered, but device notify rule not found ruleId:{}", ruleTriggerMsg.getRuleId());
                return;
            }
            SessionContextHolder.requestTenant(deviceNotifyRule.getTenant());
            var deviceId = Long.valueOf(ruleTriggerMsg.getMatchSnapshot().getFirst().getDeviceId());
            //获取报警设备信息
            var deviceDetailDTO = deviceCenterStdClient.getDeviceById(deviceId);
            if (Objects.isNull(deviceDetailDTO)) {
                log.info("IoT rule triggered, but device not found deviceId:{}", deviceId);
                return;
            }
            //发送规则触发事件
            var deviceNotifyRuleTriggerEvent = new DeviceNotifyRuleTriggerEvent(this, deviceDetailDTO, ruleTriggerSnapshotParser.parseSnapshot(deviceId, ruleTriggerMsg), deviceNotifyRule);
            this.applicationEventPublisher.publishEvent(deviceNotifyRuleTriggerEvent);
        });
    }
}
