package com.guhecloud.cube.biz.meattrade.server.third.request.statistics;

import com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig;
import com.guhecloud.cube.biz.meattrade.server.third.request.statistics.base.StatQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 食安检测统计请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TestingRankingStatRequest extends StatQueryRequest {

    @Override
    protected String getApiUrl() {
        ThirdMeatTradeConfig requestConfig = getRequestConfig();
        return requestConfig.getStatTestingRankingApiUrl();
    }
}
