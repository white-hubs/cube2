package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantSpotCheckStatisticsResponse {

    /**
     * 抽检总次数
     */
    private Integer totalQty;

    /**
     * 抽检总次数
     */
    private Integer passQty;

    /**
     * 检测合格率
     */
    private String passRate;

}
