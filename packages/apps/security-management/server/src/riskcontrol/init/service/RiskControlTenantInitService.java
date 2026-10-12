package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.init.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskLevelEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.enums.RiskWarningTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentDimensionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentLevelRuleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentMethodEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentOptionEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentDimensionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentLevelRuleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentMethodService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.service.impl.RiskAssessmentOptionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.entity.RiskWarningRuleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.warning.service.impl.RiskWarningRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风险管控租户默认配置初始化服务。
 */
@Service("securityManagementRiskControlTenantInitService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskControlTenantInitService {
    private static final List<PresetMethod> PRESET_METHODS = List.of(
            new PresetMethod("LEC", "作业条件危险性评价法",
                    "D=L*E*C，按可能性、暴露频率、后果计算作业条件危险性",
                    "L*E*C",
                    List.of(
                            new PresetDimension("L", "事故发生的可能性", "0.1~10", List.of(
                                    option("完全可以预料", "10"),
                                    option("相当可能；危害不能被发现或现场未采取防范、监测、保护、控制措施", "6"),
                                    option("可能但不经常；危害不容易被发现或控制措施未有效执行", "3"),
                                    option("可能性小，完全意外；危害容易被发现或过去曾发生类似事故", "1"),
                                    option("很不可能，可以设想；危害发生后能及时发现并定期监测", "0.5"),
                                    option("极不可能；有充分有效的防范、控制、监测、保护措施", "0.2"),
                                    option("实际不可能", "0.1"))),
                            new PresetDimension("E", "暴露于危险环境的频繁程度", "0.5~10", List.of(
                                    option("连续暴露", "10"),
                                    option("每天工作时间内暴露", "6"),
                                    option("每周一次，或偶然暴露", "3"),
                                    option("每月一次暴露", "2"),
                                    option("每年几次暴露", "1"),
                                    option("非常罕见地暴露", "0.5"))),
                            new PresetDimension("C", "发生事故产生的后果", "1~100", List.of(
                                    option("10人以上死亡，或50人以上重伤；直接经济损失≥5000万元", "100"),
                                    option("3~9人死亡，或10~49人重伤；直接经济损失1000万~5000万元", "40"),
                                    option("3人以下死亡，或10人以下重伤；直接经济损失100万~1000万元", "15"),
                                    option("伤残；直接经济损失10万~100万元", "7"),
                                    option("轻伤；直接经济损失1万~10万元", "2"),
                                    option("无伤亡；直接经济损失<1万元", "1")))),
                    List.of(
                            level(RiskLevelEnum.MAJOR.name(), "320.0001", null, "RED"),
                            level(RiskLevelEnum.LARGE.name(), "160", "320", "ORANGE"),
                            level(RiskLevelEnum.GENERAL.name(), "70", "159.9999", "YELLOW"),
                            level(RiskLevelEnum.LOW.name(), null, "69.9999", "BLUE"))),
            new PresetMethod("LS", "风险矩阵法",
                    "R=L*S，按事故发生可能性和严重性计算风险矩阵分值",
                    "L*S",
                    List.of(
                            new PresetDimension("L", "事故发生的可能性", "1~5", List.of(
                                    option("现场无防范、监测、保护、控制措施，或正常情况下经常发生事故事件", "5"),
                                    option("危害不容易被发现，未监测或控制措施未有效执行，预期情况下发生", "4"),
                                    option("无保护措施或未严格执行规程；危害容易被发现或曾发生类似事故事件", "3"),
                                    option("危害发生能及时发现并定期监测，现场防范控制措施可有效执行", "2"),
                                    option("有充分有效的防范、控制、监测、保护措施，极不可能发生", "1"))),
                            new PresetDimension("S", "后果严重性", "1~5", List.of(
                                    option("10人以上死亡，或50人以上重伤；直接经济损失≥5000万元", "5"),
                                    option("3~9人死亡，或10~49人重伤；直接经济损失1000万~5000万元", "4"),
                                    option("3人以下死亡，或10人以下重伤；直接经济损失100万~1000万元", "3"),
                                    option("伤残；直接经济损失10万~100万元", "2"),
                                    option("轻伤或无伤亡；直接经济损失<10万元", "1")))),
                    List.of(
                            level(RiskLevelEnum.MAJOR.name(), "20", null, "RED"),
                            level(RiskLevelEnum.LARGE.name(), "9", "19.9999", "ORANGE"),
                            level(RiskLevelEnum.GENERAL.name(), "4", "8.9999", "YELLOW"),
                            level(RiskLevelEnum.LOW.name(), null, "3.9999", "BLUE"))),
            new PresetMethod("MES", "风险程度评价法",
                    "R=M*E*S，按控制措施状态、暴露或出现频率、后果严重性计算风险程度",
                    "M*E*S",
                    List.of(
                            new PresetDimension("M", "控制措施的状态", "1~5", List.of(
                                    option("无控制措施；完全没有任何安全防护或管理措施", "5"),
                                    option("有减轻后果的应急措施，如警报系统、个体防护用品等", "3"),
                                    option("有预防措施，如机器防护装置等，且保证有效", "1"))),
                            new PresetDimension("E", "暴露或出现频率", "0.5~10", List.of(
                                    option("连续暴露；财产损失或环境污染常态出现", "10"),
                                    option("每天工作时间内暴露或出现", "6"),
                                    option("每周一次，或偶尔暴露/出现", "3"),
                                    option("每月一次暴露或出现", "2"),
                                    option("每年几次暴露或出现", "1"),
                                    option("更少的暴露或出现", "0.5"))),
                            new PresetDimension("S", "后果严重性", "1~10", List.of(
                                    option("多人死亡；财产损失>1000万元；重大环境影响的不可控排放", "10"),
                                    option("一人死亡或多人永久性失能；多人职业病；财产损失100万~1000万元", "8"),
                                    option("一人永久失能；一人职业病；财产损失10万~100万元", "4"),
                                    option("需医院治疗且缺工；职业性多发病；财产损失1万~10万元", "2"),
                                    option("轻微伤害仅需急救；身体不适；财产损失<1万元；无环境影响", "1")))),
                    List.of(
                            level(RiskLevelEnum.MAJOR.name(), "180.0001", null, "RED"),
                            level(RiskLevelEnum.LARGE.name(), "90", "180", "ORANGE"),
                            level(RiskLevelEnum.GENERAL.name(), "50", "89.9999", "YELLOW"),
                            level(RiskLevelEnum.LOW.name(), null, "49.9999", "BLUE"))));

    private static final List<PresetWarningRule> PRESET_WARNING_RULES = List.of(
            new PresetWarningRule(RiskWarningTypeEnum.LEVEL_UPGRADE.name(), "{\"from\":\"LOW\",\"to\":\"LARGE\"}", "RED", "APP,SYSTEM", 7),
            new PresetWarningRule(RiskWarningTypeEnum.ASSESSMENT_OVERDUE.name(), "{\"overdueDays\":7}", "ORANGE", "APP,SYSTEM", 14),
            new PresetWarningRule(RiskWarningTypeEnum.INDICATOR_EXCEED.name(), "{\"majorRate\":50,\"largeRate\":80}", "YELLOW", "APP,SYSTEM", 30));

    private final RiskAssessmentMethodService methodService;
    private final RiskAssessmentDimensionService dimensionService;
    private final RiskAssessmentOptionService optionService;
    private final RiskAssessmentLevelRuleService levelRuleService;
    private final RiskWarningRuleService warningRuleService;

    /**
     * 初始化指定租户的风险管控默认配置。
     *
     * @param tenant 租户编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void initTenantDefaults(Integer tenant) {
        initPresetAssessmentMethods(tenant);
        initPresetWarningRules(tenant);
    }

    private void initPresetAssessmentMethods(Integer tenant) {
        for (PresetMethod preset : PRESET_METHODS) {
            if (methodService.count(Wrappers.<RiskAssessmentMethodEntity>lambdaQuery()
                    .eq(RiskAssessmentMethodEntity::getMethodCode, preset.code())) > 0) {
                continue;
            }
            RiskAssessmentMethodEntity method = new RiskAssessmentMethodEntity();
            method.setId(IdWorker.getId());
            method.setMethodCode(preset.code());
            method.setMethodName(preset.name());
            method.setMethodDesc(preset.description());
            method.setFormula(preset.formula());
            method.setPresetFlag(true);
            method.setEnabled(true);
            method.setTenant(tenant);
            methodService.save(method);

            Map<String, Long> dimensionIdMap = new HashMap<>();
            List<RiskAssessmentDimensionEntity> dimensions = new ArrayList<>();
            int dimensionSortNo = 1;
            for (PresetDimension presetDimension : preset.dimensions()) {
                RiskAssessmentDimensionEntity dimension = new RiskAssessmentDimensionEntity();
                dimension.setId(IdWorker.getId());
                dimension.setMethodId(method.getId());
                dimension.setDimensionCode(presetDimension.code());
                dimension.setDimensionName(presetDimension.name());
                dimension.setScoreRange(presetDimension.scoreRange());
                dimension.setSortNo(dimensionSortNo++);
                dimension.setTenant(tenant);
                dimensions.add(dimension);
                dimensionIdMap.put(presetDimension.code(), dimension.getId());
            }
            if (!dimensions.isEmpty()) {
                dimensionService.saveBatch(dimensions);
            }

            List<RiskAssessmentOptionEntity> options = new ArrayList<>();
            for (PresetDimension presetDimension : preset.dimensions()) {
                Long dimensionId = dimensionIdMap.get(presetDimension.code());
                int optionSortNo = 1;
                for (PresetOption presetOption : presetDimension.options()) {
                    RiskAssessmentOptionEntity option = new RiskAssessmentOptionEntity();
                    option.setId(IdWorker.getId());
                    option.setDimensionId(dimensionId);
                    option.setOptionName(presetOption.name());
                    option.setScore(presetOption.score());
                    option.setSortNo(optionSortNo++);
                    option.setTenant(tenant);
                    options.add(option);
                }
            }
            if (!options.isEmpty()) {
                optionService.saveBatch(options);
            }

            List<RiskAssessmentLevelRuleEntity> levelRules = preset.levelRules().stream()
                    .map(presetRule -> toLevelRule(method.getId(), presetRule, tenant))
                    .toList();
            if (!levelRules.isEmpty()) {
                levelRuleService.saveBatch(levelRules);
            }
        }
    }

    private void initPresetWarningRules(Integer tenant) {
        for (PresetWarningRule preset : PRESET_WARNING_RULES) {
            if (warningRuleService.count(Wrappers.<RiskWarningRuleEntity>lambdaQuery()
                    .eq(RiskWarningRuleEntity::getWarningType, preset.warningType())) > 0) {
                continue;
            }
            RiskWarningRuleEntity rule = new RiskWarningRuleEntity();
            rule.setId(IdWorker.getId());
            rule.setWarningType(preset.warningType());
            rule.setConditionJson(preset.conditionJson());
            rule.setSeverity(preset.severity());
            rule.setNotifyChannels(preset.notifyChannels());
            rule.setDisposeDeadlineDays(preset.disposeDeadlineDays());
            rule.setEnabled(true);
            rule.setTenant(tenant);
            warningRuleService.save(rule);
        }
    }

    private RiskAssessmentLevelRuleEntity toLevelRule(Long methodId, PresetLevelRule presetRule, Integer tenant) {
        RiskAssessmentLevelRuleEntity rule = new RiskAssessmentLevelRuleEntity();
        rule.setId(IdWorker.getId());
        rule.setMethodId(methodId);
        rule.setRiskLevel(presetRule.riskLevel());
        rule.setMinScore(presetRule.minScore());
        rule.setMaxScore(presetRule.maxScore());
        rule.setColor(presetRule.color());
        rule.setControlRequirement(controlRequirement(presetRule.riskLevel()));
        rule.setTenant(tenant);
        return rule;
    }

    private static PresetOption option(String name, String score) {
        return new PresetOption(name, new BigDecimal(score));
    }

    private static PresetLevelRule level(String riskLevel, String minScore, String maxScore, String color) {
        return new PresetLevelRule(riskLevel,
                minScore == null ? null : new BigDecimal(minScore),
                maxScore == null ? null : new BigDecimal(maxScore),
                color);
    }

    private String controlRequirement(String riskLevel) {
        if (RiskLevelEnum.MAJOR.name().equals(riskLevel)) {
            return "重大风险，立即采取控制措施并重点跟踪";
        }
        if (RiskLevelEnum.LARGE.name().equals(riskLevel)) {
            return "较大风险，制定措施并限期整改";
        }
        if (RiskLevelEnum.GENERAL.name().equals(riskLevel)) {
            return "一般风险，落实日常管控和巡检";
        }
        return "低风险，维持现有控制措施";
    }

    private record PresetMethod(String code,
                                String name,
                                String description,
                                String formula,
                                List<PresetDimension> dimensions,
                                List<PresetLevelRule> levelRules) {
    }

    private record PresetDimension(String code,
                                   String name,
                                   String scoreRange,
                                   List<PresetOption> options) {
    }

    private record PresetOption(String name, BigDecimal score) {
    }

    private record PresetLevelRule(String riskLevel,
                                   BigDecimal minScore,
                                   BigDecimal maxScore,
                                   String color) {
    }

    private record PresetWarningRule(String warningType,
                                     String conditionJson,
                                     String severity,
                                     String notifyChannels,
                                     Integer disposeDeadlineDays) {
    }
}
