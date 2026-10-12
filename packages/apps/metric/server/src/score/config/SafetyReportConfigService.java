package com.guhecloud.cube.biz.metric.server.score.config;

import com.guhecloud.cube.biz.metric.server.rule.service.query.RuleLibraryQueryService;
import com.guhecloud.cube.biz.metric.server.rule.vo.RuleDetailVO;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreItems;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreRules;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SafetyReportConfigService implements SafetyReportConfig {

    private final RuleLibraryQueryService ruleLibraryQueryService;

    public SafetyReportConfigService(RuleLibraryQueryService ruleLibraryQueryService) {
        this.ruleLibraryQueryService = ruleLibraryQueryService;
    }

    @Override
    public Optional<ScoreRules> getScoreRules() {
        try {
            List<RuleDetailVO> configs = ruleLibraryQueryService.getDetailConfigsTree();
            if (CollectionUtils.isEmpty(configs)) {
                return Optional.empty();
            }
            ScoreRules scoreRules = new ScoreRules();
            scoreRules.setScoreItems(ObjectUtils.copying(configs, ScoreItems.class));
            return Optional.of(scoreRules);
        } catch (Exception e) {
            log.error("安全报告获取记分规则配置失败，", e);
        }
        return Optional.empty();
    }
}
