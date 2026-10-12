package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 商户食安数据统计请求参数
 *
 * @author lanchun
 * @date 2025/1/16 18:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantSpotCheckStatisticsRequest extends AbstractDbcRequest {

    /**
     * 客户id
     */
    private String merchantId;

    @Override
    public String getUrl() {
        return getUrl("/api/v2/query/getCountTesting")
                .append("?merchantId={merchantId}").toString();
    }
}
