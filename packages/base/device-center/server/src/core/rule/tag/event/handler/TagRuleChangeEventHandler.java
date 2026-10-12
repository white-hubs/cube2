package com.guhecloud.cube.base.device.center.core.rule.tag.event.handler;

import com.guhecloud.cube.base.device.center.core.model.BizTagRule;
import com.guhecloud.cube.base.device.center.core.rule.RuleCompare;
import com.guhecloud.cube.base.device.center.core.rule.RuleParse;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.TagRuleGroupCache;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.TagRuleRedisCache;
import com.guhecloud.cube.base.device.center.core.rule.tag.cache.entity.TagRule;
import com.guhecloud.cube.base.device.center.core.rule.tag.event.TagRuleChangeEvent;
import com.guhecloud.cube.base.device.center.core.service.CenterBizTagRuleService;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TagRuleChangeEventHandler {

    private final CenterBizTagRuleService centerBizTagRuleService;

    private final TagRuleRedisCache tagRuleRedisCache;

    private final TagRuleGroupCache tagRuleGroupCache;

    public TagRuleChangeEventHandler(CenterBizTagRuleService centerBizTagRuleService, RedisTemplate<String, Object> redisTemplate,
                                     TagRuleRedisCache tagRuleRedisCache, TagRuleGroupCache tagRuleGroupCache
                                    ) {
        this.centerBizTagRuleService = centerBizTagRuleService;
        this.tagRuleRedisCache = tagRuleRedisCache;
        this.tagRuleGroupCache = tagRuleGroupCache;
    }

    @EventListener
    public void tagRuleChangeEvent(final TagRuleChangeEvent event) {
        Optional<BizTagRule> bizTagRuleOptional = centerBizTagRuleService.getTagRule(event.getTagCode());
        if (!bizTagRuleOptional.isPresent()) {
            log.info("tag_rule is not found code:{}", event.getTagCode());
            return;
        }
        BizTagRule rule = bizTagRuleOptional.get();
        boolean updated = true;
        //从缓存中获取tag的上次值
        Optional<TagRule> tagRuleOptional = tagRuleRedisCache.getCache(event.getTagCode());
        RuleGroup newRuleGroup = RuleParse.parseRuleConfig(rule.getRule());
        if (tagRuleOptional.isPresent()) {
            updated = RuleCompare.hasRuleChanged(RuleParse.parseRuleConfig(tagRuleOptional.get().getRule()),
                                                 newRuleGroup);
        }
        if (updated) {
            TagRule tagRule = new TagRule(
                    rule.getTagCode(),
                    rule.getRule(),
                    true,
                    DateUtils.toTimestamp(rule.getUpdateTime()),
                    rule.getTenant());
            tagRuleChangeRefresh(tagRule, newRuleGroup);
        }
    }

    public void tagRuleChangeRefresh(TagRule rule, RuleGroup newRuleGroup) {
        //刷新内存中的条件缓存
        tagRuleGroupCache.putRuleGroup(rule.getTenant(), rule.getCode(), newRuleGroup);
        //规则存在更新刷新redis数据库
        tagRuleRedisCache.saveCache(rule);
    }
}
