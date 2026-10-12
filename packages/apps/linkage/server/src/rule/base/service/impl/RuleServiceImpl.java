package com.guhecloud.cube.biz.linkage.server.rule.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageService;
import com.guhecloud.cube.biz.linkage.server.rule.base.dao.RuleMapper;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.RuleLinkage;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleModifyParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.param.RuleTriggerParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleLinkageService;
import com.guhecloud.cube.biz.linkage.server.rule.base.service.RuleService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.RuleBusinessDynamicHandler;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessChangeHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessDelHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEnabledHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessEventHandlerRequest;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.dto.RuleBusinessIncreaseHandlerRequest;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements RuleService {

    private final RuleLinkageService ruleLinkageService;

    private final LinkageService linkageService;

    private final RuleBusinessDynamicHandler ruleBusinessDynamicHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull RuleModifyParam ruleModifyParam) {
        Rule rule = ObjectUtils.copying(ruleModifyParam.getBasicModify(), Rule.class);
        rule.setCode(String.valueOf(RedisUtils.generateIdentity()));
        rule.setRuleConfig(ruleModifyParam.getRuleConfig());
        this.baseMapper.insert(rule);
        if (!CollectionUtils.isEmpty(ruleModifyParam.getLinkageEmergencyActionModifies())) {
            //规则联动动作配置
            Long linkageId = this.linkageService.saveRuleLinkage(rule.getCode(), rule.getName(), ruleModifyParam.getLinkageEmergencyActionModifies());
            //新增规则与联动关联
            RuleLinkage ruleLinkage = new RuleLinkage(IdWorker.getId(), rule.getId(), linkageId);
            this.ruleLinkageService.save(ruleLinkage);
        }
        RuleBusinessIncreaseHandlerRequest ruleBusinessHandlerRequest = new RuleBusinessIncreaseHandlerRequest(rule.getBusinessType());
        ruleBusinessHandlerRequest.setRule(rule);
        ruleBusinessHandlerRequest.setLinkageEmergencyActionModifies(ruleModifyParam.getLinkageEmergencyActionModifies());
        //策略规则解析处理
        this.ruleBusinessDynamicHandler.ruleAnalysis(ruleBusinessHandlerRequest);
        return rule.getId();
    }

    @Override
    public Rule getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Rule getByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<Rule>lambdaQuery().eq(Rule::getCode, code));
    }

    @Override
    public List<Rule> listEnabledRulesByIds(@NonNull Collection<? extends Serializable> idList) {
        return CollectionUtils.isEmpty(idList) ? Collections.emptyList() : this.lambdaQuery()
                .eq(Rule::getEnabled, Boolean.TRUE)
                .in(Rule::getId, idList).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull RuleModifyParam ruleModifyParam) {
        Rule rule = this.getById(id);
        Assert.isNotFound(rule, "规则不存在或已被删除");
        ObjectUtils.copying(ruleModifyParam.getBasicModify(), rule);
        rule.setRuleConfig(ruleModifyParam.getRuleConfig());
        String oldCode = rule.getCode();
        rule.setCode(String.valueOf(RedisUtils.generateIdentity()));
        this.baseMapper.updateById(rule);
        this.ruleLinkageService.removeByRuleId(id);
        if (!CollectionUtils.isEmpty(ruleModifyParam.getLinkageEmergencyActionModifies())) {
            //规则联动动作配置
            Long linkageEmergencyId = this.linkageService.saveRuleLinkage(rule.getCode(), rule.getName(), ruleModifyParam.getLinkageEmergencyActionModifies());
            //新增规则与联动关联
            RuleLinkage ruleLinkage = new RuleLinkage(IdWorker.getId(), rule.getId(), linkageEmergencyId);
            this.ruleLinkageService.save(ruleLinkage);
        }
        RuleBusinessChangeHandlerRequest ruleBusinessChangeHandlerRequest = new RuleBusinessChangeHandlerRequest(rule.getBusinessType());
        ruleBusinessChangeHandlerRequest.setRule(rule);
        ruleBusinessChangeHandlerRequest.setOldCode(oldCode);
        ruleBusinessChangeHandlerRequest.setLinkageEmergencyActionModifies(ruleModifyParam.getLinkageEmergencyActionModifies());
        this.ruleBusinessDynamicHandler.ruleUpdate(ruleBusinessChangeHandlerRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTriggerNum(@NonNull Long id) {
        this.baseMapper.addTriggerNum(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(@NonNull Long id, @NonNull Boolean enabled) {
        Rule rule = this.getById(id);
        Assert.isNotFound(rule, "规则不存在或已被删除");
        rule.setEnabled(enabled);
        String oldCode = rule.getCode();
        rule.setCode(String.valueOf(RedisUtils.generateIdentity()));
        this.baseMapper.updateById(rule);
        RuleBusinessEnabledHandlerRequest ruleBusinessEnabledHandlerRequest = new RuleBusinessEnabledHandlerRequest(rule.getBusinessType());
        ruleBusinessEnabledHandlerRequest.setRule(rule);
        ruleBusinessEnabledHandlerRequest.setOldCode(oldCode);
        this.ruleBusinessDynamicHandler.ruleEnabledUpdate(ruleBusinessEnabledHandlerRequest);
    }

    @Override
    public void triggerRule(@NonNull Long id, @NonNull RuleTriggerParam param) {
        Rule rule = this.getById(id);
        Assert.isNotFound(rule, "规则不存在或已被删除");
        RuleBusinessEventHandlerRequest request = new RuleBusinessEventHandlerRequest(rule.getBusinessType());
        request.setTriggerTime(param.getTriggerTime());
        request.setTriggerJson(JsonUtils.serialize(param.getEventConfig()));
        request.setRule(rule);
        this.ruleBusinessDynamicHandler.ruleEvent(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        Rule rule = this.getById(id);
        if (Objects.nonNull(rule)) {
            DatabaseContextHolder.softDelete(this, rule);
            RuleBusinessDelHandlerRequest ruleBusinessHandlerRequest = new RuleBusinessDelHandlerRequest(rule.getBusinessType());
            ruleBusinessHandlerRequest.setRuleCode(rule.getCode());
            ruleBusinessHandlerRequest.setRuleId(rule.getId());
            //策略规则解析处理
            this.ruleBusinessDynamicHandler.ruleRemove(ruleBusinessHandlerRequest);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(@NonNull List<Long> ids) {
        ids.stream().forEach(id -> {
            this.remove(id);
        });
    }
}
