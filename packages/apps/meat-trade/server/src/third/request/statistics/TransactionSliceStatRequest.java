package com.guhecloud.cube.biz.meattrade.server.third.request.statistics;

import com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.base.StatQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易片数详细信息统计请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TransactionSliceStatRequest extends StatQueryRequest {

    @Override
    protected String getApiUrl() {
        ThirdMeatTradeConfig requestConfig = getRequestConfig();
        return requestConfig.getStatTransactionComparisonSliceApiUrl();
    }
}
