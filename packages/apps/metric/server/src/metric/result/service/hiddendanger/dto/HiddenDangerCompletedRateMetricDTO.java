package com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 隐患完成率指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 13:57
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HiddenDangerCompletedRateMetricDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 工单数
     */
    private Integer hiddenDangerNumber;

    /**
     * 完成数
     */
    private Integer hiddenDangerCompletedNumber;

    /**
     * 完成率
     */
    private Double hiddenDangerCompletedRate;

}
