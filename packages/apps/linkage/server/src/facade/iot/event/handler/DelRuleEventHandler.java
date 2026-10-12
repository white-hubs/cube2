package com.guhecloud.cube.biz.linkage.server.facade.iot.event.handler;

import com.guhecloud.cube.biz.linkage.server.facade.iot.event.DelRuleEvent;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 规则删除事件处理
 *
 * @author lanchun
 * @date 2022/6/22 19:43
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DelRuleEventHandler {

    private final IotLinkageClient iotLinkageClient;

    @EventListener
    public void synchronizedIOT(DelRuleEvent delRuleEvent) {
        this.iotLinkageClient.ruleDelete(delRuleEvent.getRuleNum());
    }
}
