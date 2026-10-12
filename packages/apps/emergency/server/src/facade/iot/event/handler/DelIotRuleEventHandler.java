package com.guhecloud.cube.biz.emergency.server.facade.iot.event.handler;

import com.guhecloud.cube.biz.emergency.server.time.domain.event.DelIotRuleGroupEvent;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DelIotRuleEventHandler {

    private final IotLinkageClient iotLinkageClient;

    /**
     * 删除预案规则条件
     *
     * @param event
     */
    @EventListener
    public void delEmergencyRule(DelIotRuleGroupEvent event) {
        this.iotLinkageClient.ruleDeleteBatch(event.getRuleIds().stream().map(ruleId -> String.valueOf(ruleId))
                                                      .collect(Collectors.toSet()));
    }
}
