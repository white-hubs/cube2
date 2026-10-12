package com.guhecloud.cube.biz.emergency.server.facade.iot.event.handler;

import com.guhecloud.cube.biz.emergency.server.facade.iot.event.IotRuleStateChangeEvent;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ChangeRuleStateParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeIotRuleStateEventHandler {

    private final IotLinkageClient iotLinkageClient;

    /**
     * 修改预案规则状态
     *
     * @param event
     */
    @EventListener
    public void changeEmergencyState(IotRuleStateChangeEvent event) {
        ChangeRuleStateParam changeRuleStateParam = new ChangeRuleStateParam();
        changeRuleStateParam.setRuleNums(event.getRuleIds().stream().map(String::valueOf).collect(Collectors.toList()));
        changeRuleStateParam.setEnabled(event.getState());
        this.iotLinkageClient.changeRuleStateBatch(changeRuleStateParam);
    }
}
