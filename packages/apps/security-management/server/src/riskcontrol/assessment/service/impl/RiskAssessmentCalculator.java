package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.assessment.service.impl;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.entity.RiskAssessmentLevelRuleEntity;
import com.guhecloud.platform.base.standard.exception.Assert;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

@Component("securityManagementRiskAssessmentCalculator")
public class RiskAssessmentCalculator {
    private static final int DIVIDE_SCALE = 8;

    public BigDecimal calculate(String formula, Map<String, BigDecimal> dimensionScores, BigDecimal explicitScore) {
        if (formula == null || formula.isBlank()) {
            Assert.isParameterError(explicitScore == null, "评估公式不能为空");
            return explicitScore;
        }
        Assert.isParameterError(dimensionScores == null || dimensionScores.isEmpty(), "评估分值不能为空");
        return new FormulaParser(formula.replace('×', '*').replace('÷', '/'), dimensionScores).parse();
    }

    public String hitLevel(Collection<RiskAssessmentLevelRuleEntity> rules, BigDecimal score) {
        return rules.stream()
                .filter(rule -> rule.getMinScore() == null || score.compareTo(rule.getMinScore()) >= 0)
                .filter(rule -> rule.getMaxScore() == null || score.compareTo(rule.getMaxScore()) <= 0)
                .max(Comparator.comparing(RiskAssessmentLevelRuleEntity::getMinScore,
                        Comparator.nullsFirst(Comparator.naturalOrder())))
                .map(RiskAssessmentLevelRuleEntity::getRiskLevel)
                .orElse(null);
    }

    private static final class FormulaParser {
        private final String formula;
        private final Map<String, BigDecimal> dimensionScores;
        private int index;

        private FormulaParser(String formula, Map<String, BigDecimal> dimensionScores) {
            this.formula = formula;
            this.dimensionScores = dimensionScores;
        }

        private BigDecimal parse() {
            BigDecimal value = parseExpression();
            skipWhitespace();
            Assert.isParameterError(index != formula.length(), "评估公式非法");
            return value;
        }

        private BigDecimal parseExpression() {
            BigDecimal value = parseTerm();
            while (true) {
                skipWhitespace();
                if (match('+')) {
                    value = value.add(parseTerm());
                } else if (match('-')) {
                    value = value.subtract(parseTerm());
                } else {
                    return value;
                }
            }
        }

        private BigDecimal parseTerm() {
            BigDecimal value = parseFactor();
            while (true) {
                skipWhitespace();
                if (match('*')) {
                    value = value.multiply(parseFactor());
                } else if (match('/')) {
                    BigDecimal divisor = parseFactor();
                    Assert.isParameterError(BigDecimal.ZERO.compareTo(divisor) == 0, "评估公式除数不能为 0");
                    value = value.divide(divisor, DIVIDE_SCALE, RoundingMode.HALF_UP);
                } else {
                    return value;
                }
            }
        }

        private BigDecimal parseFactor() {
            skipWhitespace();
            if (match('+')) {
                return parseFactor();
            }
            if (match('-')) {
                return parseFactor().negate();
            }
            if (match('(')) {
                BigDecimal value = parseExpression();
                Assert.isParameterError(!match(')'), "评估公式括号不匹配");
                return value;
            }
            if (index < formula.length() && Character.isDigit(formula.charAt(index))) {
                return parseNumber();
            }
            return parseVariable();
        }

        private BigDecimal parseNumber() {
            int start = index;
            while (index < formula.length()) {
                char ch = formula.charAt(index);
                if (!Character.isDigit(ch) && ch != '.') {
                    break;
                }
                index++;
            }
            try {
                return new BigDecimal(formula.substring(start, index));
            } catch (NumberFormatException e) {
                Assert.isParameterError(true, "评估公式数字非法");
                return BigDecimal.ZERO;
            }
        }

        private BigDecimal parseVariable() {
            int start = index;
            if (index < formula.length() && Character.isLetter(formula.charAt(index))) {
                index++;
                while (index < formula.length()) {
                    char ch = formula.charAt(index);
                    if (!Character.isLetterOrDigit(ch) && ch != '_') {
                        break;
                    }
                    index++;
                }
            }
            Assert.isParameterError(start == index, "评估公式非法");
            String code = formula.substring(start, index);
            BigDecimal score = dimensionScores.get(code);
            Assert.isParameterError(score == null, "评估维度分值缺失：" + code);
            return score;
        }

        private void skipWhitespace() {
            while (index < formula.length() && Character.isWhitespace(formula.charAt(index))) {
                index++;
            }
        }

        private boolean match(char expected) {
            skipWhitespace();
            if (index < formula.length() && formula.charAt(index) == expected) {
                index++;
                return true;
            }
            return false;
        }
    }
}
