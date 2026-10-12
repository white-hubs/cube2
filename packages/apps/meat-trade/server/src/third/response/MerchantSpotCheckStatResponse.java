package com.guhecloud.cube.biz.meattrade.server.third.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantSpotCheckStatResponse {

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
