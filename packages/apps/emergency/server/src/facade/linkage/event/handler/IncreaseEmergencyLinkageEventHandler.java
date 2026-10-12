package com.guhecloud.cube.biz.emergency.server.facade.linkage.event.handler;

import com.guhecloud.cube.biz.emergency.server.rule.domain.event.IncreaseEmergencyRuleEvent;
import com.guhecloud.cube.biz.linkage.client.LinkageClient;
import com.guhecloud.cube.biz.linkage.client.param.LinkageModifyParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseEmergencyLinkageEventHandler {

    private final LinkageClient linkageClient;

    /**
     * 下发保存规则动作
     *
     * @param increaseEmergencyRuleEvent
     */
    @EventListener
    public void issuedEmergencyRuleAction(IncreaseEmergencyRuleEvent increaseEmergencyRuleEvent) {
        var increaseEmergencyActionCommands = increaseEmergencyRuleEvent.getIncreaseEmergencyRuleCommand()
                .getEmergencyActions();
        var emergencyActionBeforeList = increaseEmergencyRuleEvent.getIncreaseEmergencyRuleCommand()
                .getEmergencyActionBeforeList();
        var linkageModifyParam = new LinkageModifyParam(increaseEmergencyRuleEvent.getEmergencyRuleId(),
                                                        increaseEmergencyRuleEvent.getEmergencyRuleName(),
                                                        emergencyActionBeforeList,
                                                        increaseEmergencyActionCommands);
        this.linkageClient.saveRuleLinkage(linkageModifyParam);
    }
}
