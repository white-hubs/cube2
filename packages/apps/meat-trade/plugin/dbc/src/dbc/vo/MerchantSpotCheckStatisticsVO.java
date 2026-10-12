package com.guhecloud.cube.sn.meattrade.plugin.dbc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商户食安数据统计
 *
 * @author lanchun
 * @date 2025/1/20 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantSpotCheckStatisticsVO {

    /**
     * 抽检总次数
     */
    private Integer totalQty;

    /**
     * 通过总次数
     */
    private Integer passQty;

    /**
     * 检测合格率
     */
    private String passRate;
}
