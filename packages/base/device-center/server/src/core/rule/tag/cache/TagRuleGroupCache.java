package com.guhecloud.cube.base.device.center.core.rule.tag.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.guhecloud.cube.base.device.center.core.model.BizTagRule;
import com.guhecloud.cube.base.device.center.core.rule.RuleParse;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.cube.base.device.center.core.service.CenterBizTagRuleService;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TagRuleGroupCache {

    private final Cache<Integer, Map<String, RuleGroup>> ruleGroupCache;

    private final CenterBizTagRuleService centerBizTagRuleService;

    public TagRuleGroupCache(CenterBizTagRuleService centerBizTagRuleService) {
        this.ruleGroupCache = CacheBuilder.newBuilder()
                .maximumSize(100) // 设置最大缓存项数
                .build();
        this.centerBizTagRuleService = centerBizTagRuleService;
    }

    private Map<String, RuleGroup> getRuleGroupCache(Integer tenant) {
        return ruleGroupCache.getIfPresent(tenant);
    }

    private Map<String, RuleGroup> getRuleGroupCacheIfNotExitCreate(Integer tenant) {
        Map<String, RuleGroup> ifPresent = ruleGroupCache.getIfPresent(tenant);
        if (MapUtils.isEmpty(ifPresent)) {
            List<BizTagRule> all = centerBizTagRuleService.getAll();
            for (BizTagRule bizTagRule : all) {
                putRuleGroup(bizTagRule.getTenant(),
                             bizTagRule.getTagCode(),
                             RuleParse.parseRuleConfig(bizTagRule.getRule()));
            }
        }
        return ruleGroupCache.getIfPresent(tenant);
    }

    /**
     * 获取缓存条件
     *
     * @param key 缓存key
     *
     * @return RuleGroup
     */
    public RuleGroup getRuleGroup(Integer tenant, String key) {
        Map<String, RuleGroup> present = getRuleGroupCache(tenant);
        if (MapUtils.isNotEmpty(present)) {
            return present.get(key);
        }
        return null;
    }

    /**
     * 新增缓存
     *
     * @param tenant    租户编号
     * @param key       缓存key
     * @param ruleGroup 缓存条件
     */
    public void putRuleGroup(Integer tenant, String key, RuleGroup ruleGroup) {
        Map<String, RuleGroup> present = getRuleGroupCache(tenant);
        if (MapUtils.isEmpty(present)) {
            present = new HashMap<>();
        }
        present.putIfAbsent(key, ruleGroup);
        ruleGroupCache.put(tenant, present);
    }

    public Map<String, RuleGroup> getAll(Integer tenant) {
        return getRuleGroupCacheIfNotExitCreate(tenant);
    }
}
