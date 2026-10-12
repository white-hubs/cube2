package com.guhecloud.cube.biz.metric.server.rule.service.query;

import com.guhecloud.cube.biz.metric.server.rule.repository.RuleCoefficientRepository;
import com.guhecloud.cube.biz.metric.server.rule.repository.RuleLibraryRepository;
import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleCoefficientPO;
import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleLibraryPO;
import com.guhecloud.cube.biz.metric.server.rule.vo.RuleCoefficientVO;
import com.guhecloud.cube.biz.metric.server.rule.vo.RuleDetailVO;
import com.guhecloud.cube.biz.metric.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RuleLibraryQueryService {

    private final RuleLibraryRepository ruleLibraryRepository;

    private final RuleCoefficientRepository ruleCoefficientRepository;

    public RuleLibraryQueryService(RuleLibraryRepository ruleLibraryRepository, RuleCoefficientRepository ruleCoefficientRepository) {
        this.ruleLibraryRepository = ruleLibraryRepository;
        this.ruleCoefficientRepository = ruleCoefficientRepository;
    }

    public List<RuleVO> getConfigsTree() {
        List<RuleLibraryPO> configs = this.ruleLibraryRepository.getConfigs();
        List<RuleVO> ruleVOS = ObjectUtils.copying(configs, RuleVO.class);
        return TreeUtils.assembleTrees(ruleVOS);
    }

    public List<RuleDetailVO> getDetailConfigsTree() {
        List<RuleLibraryPO> configs = this.ruleLibraryRepository.getConfigs();
        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyList();
        }
        List<RuleDetailVO> details = ObjectUtils.copying(configs, RuleDetailVO.class);
        Set<Long> ruleIds = configs.stream().map(RuleLibraryPO::getId).collect(Collectors.toSet());
        Map<Long, List<RuleCoefficientPO>> scoreRangesMap = this.ruleCoefficientRepository.getRuleCoefficients(ruleIds).stream()
                .collect(Collectors.groupingBy(RuleCoefficientPO::getRuleId));
        details.forEach(config -> {
            if (scoreRangesMap.containsKey(config.getId())) {
                config.setCoefficients(ObjectUtils.copying(scoreRangesMap.get(config.getId()), RuleCoefficientVO.class));
            }
        });
        return TreeUtils.assembleTrees(details);
    }

    public RuleDetailVO getDetail(Long id) {
        RuleLibraryPO reportRulePO = ruleLibraryRepository.findById(id);
        if (Objects.isNull(reportRulePO)) {
            return null;
        }
        RuleDetailVO ruleDetailVO = ObjectUtils.copying(reportRulePO, RuleDetailVO.class);
        List<RuleCoefficientPO> scoreRanges = ruleCoefficientRepository.getRuleCoefficients(id);
        if (CollectionUtils.isEmpty(scoreRanges)) {
            return ruleDetailVO;
        }
        ruleDetailVO.setCoefficients(ObjectUtils.copying(scoreRanges, RuleCoefficientVO.class));
        return ruleDetailVO;
    }
}
