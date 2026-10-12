package com.guhecloud.cube.biz.metric.server.rule.service.command;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.metric.server.rule.command.ChangeRuleConfigCommand;
import com.guhecloud.cube.biz.metric.server.rule.command.DelRuleConfigCommand;
import com.guhecloud.cube.biz.metric.server.rule.command.IncreaseRuleConfigCommand;
import com.guhecloud.cube.biz.metric.server.rule.command.SaveRuleCoefficientsCommand;
import com.guhecloud.cube.biz.metric.server.rule.repository.RuleCoefficientRepository;
import com.guhecloud.cube.biz.metric.server.rule.repository.RuleLibraryRepository;
import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleCoefficientPO;
import com.guhecloud.cube.biz.metric.server.rule.repository.po.RuleLibraryPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RuleLibraryCommandService {

    private final RuleLibraryRepository ruleLibraryRepository;

    private final RuleCoefficientRepository ruleCoefficientRepository;

    public RuleLibraryCommandService(RuleLibraryRepository ruleLibraryRepository,
                                     RuleCoefficientRepository ruleCoefficientRepository
                                    ) {
        this.ruleLibraryRepository = ruleLibraryRepository;
        this.ruleCoefficientRepository = ruleCoefficientRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long increaseConfig(IncreaseRuleConfigCommand increaseRuleConfigCommand) {
        RuleLibraryPO ruleLibraryPO = ObjectUtils.copying(increaseRuleConfigCommand, RuleLibraryPO.class);
        ruleLibraryPO.setId(IdWorker.getId());
        if (StringUtils.isBlank(increaseRuleConfigCommand.getItemCode())) {
            ruleLibraryPO.setItemCode(ruleLibraryPO.getId().toString());
        }
        if (Objects.nonNull(increaseRuleConfigCommand.getParentId())) {
            RuleLibraryPO reportRulePO = ruleLibraryRepository.findById(increaseRuleConfigCommand.getParentId());
            ruleLibraryPO.setItemCodeConcat(reportRulePO.getItemCodeConcat() + "::" + ruleLibraryPO.getItemCode());
        } else {
            ruleLibraryPO.setItemCodeConcat(ruleLibraryPO.getItemCode());
        }
        Long id = this.ruleLibraryRepository.insertConfig(ruleLibraryPO);
        saveScoreRanges(ruleLibraryPO.getId(), increaseRuleConfigCommand.getCoefficients());
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long changeConfig(ChangeRuleConfigCommand changeRuleConfigCommand) {
        RuleLibraryPO reportRulePO = this.ruleLibraryRepository.findById(changeRuleConfigCommand.getId());
        if (Objects.isNull(reportRulePO)) {
            return 0L;
        }
        ObjectUtils.copying(changeRuleConfigCommand, reportRulePO);
        Long id = this.ruleLibraryRepository.updateConfig(reportRulePO);
        this.ruleCoefficientRepository.removeRuleCoefficients(reportRulePO.getId());
        saveScoreRanges(reportRulePO.getId(), changeRuleConfigCommand.getCoefficients());
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeConfig(DelRuleConfigCommand delRuleConfigCommand) {
        RuleLibraryPO reportRulePO = this.ruleLibraryRepository.findById(delRuleConfigCommand.getId());
        if (Objects.nonNull(reportRulePO)) {
            ruleLibraryRepository.removeConfig(reportRulePO.getId());
            this.ruleCoefficientRepository.removeRuleCoefficients(reportRulePO.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveScoreRanges(Long ruleId, List<SaveRuleCoefficientsCommand> changeRuleConfigCommand) {
        if (CollectionUtils.isNotEmpty(changeRuleConfigCommand)) {
            List<RuleCoefficientPO> ranges = changeRuleConfigCommand.stream().map(score -> {
                RuleCoefficientPO ruleCoefficientPO = ObjectUtils.copying(score, RuleCoefficientPO.class);
                ruleCoefficientPO.setRuleId(ruleId);
                return ruleCoefficientPO;
            }).collect(Collectors.toList());
            this.ruleCoefficientRepository.saveRuleCoefficients(ranges);
        }
    }
}
