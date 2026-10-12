package com.guhecloud.cube.base.device.center.core.rule.tag.cache;

import com.guhecloud.cube.base.device.center.core.model.BizTagRule;
import com.guhecloud.cube.base.device.center.core.rule.RuleParse;
import com.guhecloud.cube.base.device.center.core.service.CenterBizTagRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 程序启动完成缓存标签规则条件
 *
 * @author lanchun
 * @date 2025/4/10 17:15
 */
@Slf4j
@Component
public class TagRuleGroupCacheStartRunner {

    private final CenterBizTagRuleService centerBizTagRuleService;

    private final TagRuleGroupCache tagRuleGroupCache;

    public TagRuleGroupCacheStartRunner(CenterBizTagRuleService centerBizTagRuleService, TagRuleGroupCache tagRuleGroupCache
                                       ) {
        this.centerBizTagRuleService = centerBizTagRuleService;
        this.tagRuleGroupCache = tagRuleGroupCache;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void deviceTagRuleCache() {
        List<BizTagRule> all = centerBizTagRuleService.getAll();
        for (BizTagRule bizTagRule : all) {
            tagRuleGroupCache.putRuleGroup(bizTagRule.getTenant(),
                                           bizTagRule.getTagCode(),
                                           RuleParse.parseRuleConfig(bizTagRule.getRule()));
        }
    }
}
