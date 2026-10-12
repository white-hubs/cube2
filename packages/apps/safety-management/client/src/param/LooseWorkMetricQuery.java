package com.guhecloud.cube.base.dataintegrate.client.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:58
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LooseWorkMetricQuery extends BaseStatisticsQuery {
    /**
     * 有问题的，为 null 不过滤
     */
    private Boolean problematic;
}
