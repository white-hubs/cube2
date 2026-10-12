package com.guhecloud.cube.biz.linkage.server.facade.iot.event.handler;

import com.guhecloud.cube.biz.linkage.server.facade.iot.event.ChangeRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.IncreaseRuleEvent;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeRuleEventHandler {

    private final IotLinkageClient iotLinkageClient;

    private final IncreaseRuleEventHandler increaseRuleEvent;

    @EventListener
    public void synchronizedIOT(ChangeRuleEvent changeRuleEvent) {
        //删除规则
        this.iotLinkageClient.ruleDelete(changeRuleEvent.getOldCode());
        //保存规则
        RuleCreateParam ruleCreateParam = increaseRuleEvent.buildRuleCreateParam(ObjectUtils.copying(changeRuleEvent, IncreaseRuleEvent.class));
        this.iotLinkageClient.ruleAdd(ruleCreateParam);
    }
}
