package com.guhecloud.cube.biz.emergency.server.time.domain.event.handler;

import com.guhecloud.cube.biz.emergency.server.rule.domain.aggregates.EmergencyRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeEmergencyRuleEventHandler {

    private final DelEmergencyRuleEventHandler delEmergencyRuleEventHandler;

    private final IncreaseEmergencyRuleEventHandler increaseEmergencyRuleEventHandler;

    public void changeEmergencyRuleEvent(final EmergencyRule emergencyRule) {
        //先移除掉原先的job
        this.delEmergencyRuleEventHandler.delEmergencyRuleEvent(emergencyRule);
        //重新新增job
        this.increaseEmergencyRuleEventHandler.saveEmergencyRuleJob(emergencyRule);
    }
}
