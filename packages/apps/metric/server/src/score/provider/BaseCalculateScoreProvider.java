package com.guhecloud.cube.biz.metric.server.score.provider;

import com.guhecloud.cube.biz.metric.server.score.config.DefaultSafetyReportScoreTopicConfig;
import com.guhecloud.cube.biz.metric.server.score.config.SafetyReportConfigService;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreItems;
import com.guhecloud.cube.biz.metric.server.score.config.dto.ScoreRange;
import com.guhecloud.cube.biz.metric.server.score.dto.TopicCalculateScore;
import com.guhecloud.cube.biz.metric.server.score.param.CalculateParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

public abstract class BaseCalculateScoreProvider extends DefaultSafetyReportScoreTopicConfig implements CalculateScoreProvider {

    public BaseCalculateScoreProvider(SafetyReportConfigService safetyReportConfigService
                                     ) {
        super(safetyReportConfigService);
    }

    /**
     * 得分计算
     *
     * @param param 计算参数
     *
     * @return
     */
    @Override
    public TopicCalculateScore calculateScore(CalculateParam param) {
        Optional<ScoreItems> topicScoreConfig = getTopicScoreConfig(param);
        return topicScoreConfig.map(scoreItems -> calculateScore(param, scoreItems)).orElse(null);
    }

    private TopicCalculateScore calculateScore(CalculateParam param, ScoreItems scoreItems) {
        param.setComputeMetric(scoreItems.getComputeMetric());
        TopicCalculateScore detail = calculateResult(param);
        detail.setTopic(scoreItems.getItemCode());
        detail.setTopicName(scoreItems.getItemName());
        detail.setWeightAllot(scoreItems.getWeightAllot());
        return calculateScoreRangeValue(scoreItems, detail);
    }

    /**
     * 计算得分
     *
     * @param scoreItems 主题分值区间配置
     * @param detail     计算详情结果
     *
     * @return 得分
     */
    private TopicCalculateScore calculateScoreRangeValue(ScoreItems scoreItems, TopicCalculateScore detail) {
        double score;
        double weightScore;
        double scoreRatio = 0.0;
        double result = detail.getResult();
        for (ScoreRange scoreRange : scoreItems.getCoefficients()) {
            if ((scoreRange.getGreaterThan() == null || result > scoreRange.getGreaterThan()) &&
                (scoreRange.getLessThanOrEqual() == null || result <= scoreRange.getLessThanOrEqual())) {
                scoreRatio = scoreRange.getScoreRatio();
                break;
            }
        }
        score = BigDecimal.valueOf(100 * scoreRatio).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //获取所有父级的权重配置
        List<ScoreItems> allParentItems = findAllParentItems(scoreItems.getItemCodeConcat());
        double parentWeight = multiplyPropertyValues(allParentItems, ScoreItems::getWeightAllot, 1);
        weightScore = BigDecimal.valueOf(score).multiply(BigDecimal.valueOf(parentWeight))
                .multiply(BigDecimal.valueOf(scoreItems.getWeightAllot()).multiply(BigDecimal.valueOf(0.01)))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
        detail.setScore(score);
        detail.setWeightScore(weightScore);
        return detail;
    }

    /**
     * 计算详情结果
     *
     * @param param
     *
     * @return
     */
    protected abstract TopicCalculateScore calculateResult(CalculateParam param);

    /**
     * 计算列表中对象某个属性的乘积
     *
     * @param list           对象列表
     * @param valueExtractor 属性提取函数
     * @param defaultValue   默认值（当列表为空时返回）
     *
     * @return 所有对象属性的乘积
     */
    public static <T> double multiplyPropertyValues(
            List<T> list,
            ToDoubleFunction<T> valueExtractor,
            double defaultValue
                                                   ) {
        // 处理空列表或空提取函数
        if (list == null || list.isEmpty() || valueExtractor == null) {
            return defaultValue;
        }

        double result = 1.0;

        for (T item : list) {
            if (item != null) {
                double value = valueExtractor.applyAsDouble(item) * 0.01;
                result *= value; // 累乘
            }
        }

        return result;
    }
}
