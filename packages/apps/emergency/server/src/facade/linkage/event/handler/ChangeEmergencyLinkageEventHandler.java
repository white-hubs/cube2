package com.guhecloud.cube.biz.emergency.server.facade.linkage.event.handler;

import com.guhecloud.cube.biz.emergency.server.rule.domain.event.ChangeEmergencyRuleEvent;
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
public class ChangeEmergencyLinkageEventHandler {

    private final LinkageClient linkageClient;

    /**
     * 下发修改规则动作
     *
     * @param changeEmergencyRuleEvent
     */
    @EventListener
    public void changeEmergencyRuleAction(ChangeEmergencyRuleEvent changeEmergencyRuleEvent) {
        var linkageActionModifyParams = changeEmergencyRuleEvent.getChangeEmergencyRuleCommand().getEmergencyActions();
        var emergencyActionBeforeList = changeEmergencyRuleEvent.getChangeEmergencyRuleCommand()
                .getEmergencyActionBeforeList();
        var linkageModifyParam = new LinkageModifyParam(changeEmergencyRuleEvent.getEmergencyRuleId(),
                                                        changeEmergencyRuleEvent.getEmergencyRuleName(),
                                                        emergencyActionBeforeList,
                                                        linkageActionModifyParams);
        this.linkageClient.updateLinkage(linkageModifyParam);
    }
}
