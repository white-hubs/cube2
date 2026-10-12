package com.guhecloud.cube.biz.metric.server.metric.result.service.hiddendanger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 隐患数量指标
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/6 16:31
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HiddenDangerNumMetricDTO {

    private Integer hiddenDangerNumber;
}
