package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.handler;

import com.guhecloud.cube.biz.linkage.server.linkage.service.impl.EventTriggerService;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.dto.RuleSceneDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.dto.RuleSceneEventDTO;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.entity.RuleScene;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.scene.service.RuleSceneService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
public class RuleSceneHandler extends RuleBusinessAbstractHandler {

    @Autowired
    private RuleSceneService ruleSceneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventTriggerService eventTriggerService;

    @Autowired
    private RuleService ruleService;

    public RuleSceneHandler() {
        super(BusinessTypeEnum.SCENE_CONTROL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleAnalysis(RuleBusinessIncreaseHandlerRequest request) {
        Rule rule = request.getRule();
        //获取规则json
        String ruleConfigStr = rule.getRuleConfig();
        RuleSceneDTO ruleSceneDTO = JsonUtils.deserialize(ruleConfigStr, RuleSceneDTO.class);
        String password = null;
        if (Objects.nonNull(ruleSceneDTO.getPassword())) {
            Assert.isParameterError(!Objects.equals(ruleSceneDTO.getPassword(), ruleSceneDTO.getAgainPassword()), "输入的密码不一致!");
            password = this.passwordEncoder.encode(ruleSceneDTO.getPassword());
        }
        RuleScene ruleScene = RuleScene.builder()
                .password(password)
                .ruleId(request.getRule().getId())
                .build();
        this.ruleSceneService.save(ruleScene);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleUpdate(RuleBusinessChangeHandlerRequest request) {
        RuleScene ruleScene = this.ruleSceneService.getByRuleId(request.getRule().getId());
        String password = null;
        RuleSceneDTO ruleSceneDTO = JsonUtils.deserialize(request.getRule().getRuleConfig(), RuleSceneDTO.class);
        if (Objects.nonNull(ruleSceneDTO.getPassword())) {
            Assert.isParameterError(!Objects.equals(ruleSceneDTO.getPassword(), ruleSceneDTO.getAgainPassword()), "输入的密码不一致!");
            password = this.passwordEncoder.encode(ruleSceneDTO.getPassword());
        }
        ruleScene.setPassword(password);
        this.ruleSceneService.updateById(ruleScene);
    }

    @Override
    public void ruleEnabledUpdate(RuleBusinessEnabledHandlerRequest request) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleRemove(RuleBusinessDelHandlerRequest request) {
        this.ruleSceneService.removeByRuleId(request.getRuleId());
    }

    @Override
    public void ruleEvent(RuleBusinessEventHandlerRequest request) {
        SessionContextHolder.requestTenant(request.getRule().getTenant());
        RuleSceneEventDTO ruleSceneEventDTO = RuleSceneEventDTO.builder()
                .name(request.getRule().getName())
                .eventName(request.getRule().getName())
                .triggerTime(DateUtils.format(request.getTriggerTime()))
                .operatorName(SecurityContextHolder.getPrincipal().getName())
                .build();
        this.ruleService.addTriggerNum(request.getRule().getId());
        //生成事件
        this.eventTriggerService.eventTrigger(Collections.singletonList(request.getRule()), request.getTriggerTime(), JsonUtils.serialize(ruleSceneEventDTO));
    }
}
