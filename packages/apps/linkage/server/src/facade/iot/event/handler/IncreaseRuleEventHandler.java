package com.guhecloud.cube.biz.linkage.server.facade.iot.event.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.linkage.server.facade.iot.event.IncreaseRuleEvent;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.ComboRuleRequestRequest;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.PropertySetActionRequest;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.RuleConfigRequest;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.SimpleRuleRequest;
import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 规则下发事件处理
 *
 * @author lanchun
 * @date 2022/6/22 18:25
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseRuleEventHandler {

    private final IotLinkageClient iotLinkageClient;

    @EventListener
    public void synchronizedIOT(IncreaseRuleEvent increaseRuleEvent) {
        RuleCreateParam ruleCreateParam = this.buildRuleCreateParam(increaseRuleEvent);
        this.iotLinkageClient.ruleAdd(ruleCreateParam);
    }

    public RuleCreateParam buildRuleCreateParam(IncreaseRuleEvent increaseRuleEvent) {
        RuleCreateParam ruleCreateParam = new RuleCreateParam();
        //物联设备条件规则需要下发到IOT/RE规则中
        RuleConfigRequest ruleConfigRequest = buildRuleConfig(increaseRuleEvent);
        ruleCreateParam.setRuleNum(increaseRuleEvent.getCode());
        ruleCreateParam.setRuleName(increaseRuleEvent.getName());
        ruleCreateParam.setRuleConfig((JSONObject) JSON.toJSON(ruleConfigRequest));
        ruleCreateParam.setEnabled(increaseRuleEvent.getEnabled().toString());
        ruleCreateParam.setTenantId(SessionContextHolder.getRequester().getTenant().toString());
        return ruleCreateParam;
    }

    private RuleConfigRequest buildRuleConfig(IncreaseRuleEvent increaseRuleEvent) {
        RuleConfigRequest ruleConfigRequest = new RuleConfigRequest();
        ComboRuleRequestRequest comboRuleRequest = new ComboRuleRequestRequest();
        List<Object> rules = new ArrayList<>();
        increaseRuleEvent.getDeviceProEvents().stream().forEach(deviceProEvent -> {
            SimpleRuleRequest simpleRule = new SimpleRuleRequest();
            simpleRule.setDeviceId(deviceProEvent.getDeviceCode());
            simpleRule.setPropCode(deviceProEvent.getPropCode());
            simpleRule.setOp(deviceProEvent.getOp());
            simpleRule.setOprand(Collections.singletonList(deviceProEvent.getOprand()));
            simpleRule.setDelayTime(deviceProEvent.getDelayTime());
            rules.add(simpleRule);
        });
        comboRuleRequest.setOp(increaseRuleEvent.getOp());
        comboRuleRequest.setRules(rules);
        List<Object> actions = new ArrayList<>();
        increaseRuleEvent.getIotDeviceProDTOS().stream().forEach(iotDeviceAction -> {
            PropertySetActionRequest propertySetActionRequest = new PropertySetActionRequest(iotDeviceAction.getCode(), iotDeviceAction.getPropCode(), iotDeviceAction.getPropValue());
            actions.add(propertySetActionRequest);
        });
        ruleConfigRequest.setRule(comboRuleRequest);
        ruleConfigRequest.setAction(actions);
        return ruleConfigRequest;
    }
}
