package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 13:51
 * @Description:
 */
@Data
public class HiddenDangerNumStatisticsVO {
    /**
     * 隐患总数
     */
    private Integer total;
    /**
     * 已完成隐患数
     */
    private Integer finished;
    /**
     * 处理中隐患数
     */
    private Integer inHand;

    /**
     * 获取完成率
     *
     * @return
     */
    public Double getCalculateProportion() {
        if (Objects.isNull(total) || Objects.isNull(finished)) {
            return 0d;
        }
        if (total == 0) {
            return 0d;
        }
        if (total > 0 && 0 < finished) {
            //计算占比
            return new BigDecimal(finished)
                    .divide(new BigDecimal(total), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100)).doubleValue();
        }
        return 0d;
    }
}
