package com.guhecloud.cube.biz.emergency.server.facade.linkage.event.handler;

import com.guhecloud.cube.biz.emergency.server.rule.domain.event.DelEmergencyRuleEvent;
import com.guhecloud.cube.biz.linkage.client.LinkageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DelEmergencyLinkageEventHandler {

    private final LinkageClient linkageClient;

    /**
     * 删除预案规则动作
     *
     * @param delEmergencyRuleEvent
     */
    @EventListener
    public void delEmergencyRuleAction(DelEmergencyRuleEvent delEmergencyRuleEvent) {
        //动作删除
        this.linkageClient.removeByRuleIds(delEmergencyRuleEvent.getRuleIds());
    }
}
