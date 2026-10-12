package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.RangeConfigDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 扣分匹配器
 * 根据采集值和配置的扣分区间匹配对应的扣分值
 */
@Component
public class DeductionMatcher {
    /**
     * 匹配扣分值
     * 根据配置区间和实际值找到对应的扣分
     *
     * @param rangeConfigJson 扣分区间配置 JSON
     * @param value           实际采集值
     *
     * @return 扣分匹配结果
     */
    public DeductionMatchResult match(String rangeConfigJson, BigDecimal value) {
        // 处理空值情况，将 null 转换为 0
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        // 解析 JSON 字符串为扣分区间配置列表
        List<RangeConfigDTO> ranges = JSON.parseArray(rangeConfigJson, RangeConfigDTO.class);
        // 如果配置为空，返回默认结果（不扣分）
        if (ranges == null || ranges.isEmpty()) {
            return new DeductionMatchResult(BigDecimal.ZERO, "");
        }
        // 遍历所有扣分区间
        for (RangeConfigDTO range : ranges) {
            // 跳过无效的区间配置（起始值、结束值或扣分值任一为空则无效）
            if (range.getStartValue() == null || range.getEndValue() == null || range.getDeductScore() == null) {
                continue;
            }
            // 判断采集值是否在当前区间内
            if (value.compareTo(range.getStartValue()) >= 0 && value.compareTo(range.getEndValue()) < 0) {
                // 匹配成功，返回扣分值和区间描述文本
                return new DeductionMatchResult(range.getDeductScore(),
                                                String.format("%s-%s", range.getStartValue().stripTrailingZeros().toPlainString(),
                                                              range.getEndValue().stripTrailingZeros().toPlainString()));
            }
        }
        // 未匹配到任何区间，返回默认结果（不扣分）
        return new DeductionMatchResult(BigDecimal.ZERO, "");
    }
}
