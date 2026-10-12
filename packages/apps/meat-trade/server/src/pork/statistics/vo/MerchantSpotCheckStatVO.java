package com.guhecloud.cube.biz.meattrade.server.pork.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商户食安抽检统计
 *
 * @author lanchun
 * @date 2025/1/23 14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantSpotCheckStatVO {

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
