package com.guhecloud.cube.biz.metric.server.score.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class CalculateParam {

    /**
     * 一次报告生成的唯一id
     */
    private String requestId;

    /**
     * 分项code Concat
     */
    private String itemCodeConcat;

    /**
     * 指标主键
     */
    private Long computeMetric;

    /**
     * 时间范围
     */
    private Period period;
}
