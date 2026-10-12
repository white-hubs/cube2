package com.guhecloud.cube.biz.meattrade.server.third.request.statistics;

import com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.base.StatQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易佣金详细信息统计请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TransactionCommissionStatRequest extends StatQueryRequest {

    @Override
    protected String getApiUrl() {
        ThirdMeatTradeConfig requestConfig = getRequestConfig();
        return requestConfig.getStatTransactionComparisonCommissionApiUrl();
    }
}
