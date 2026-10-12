package com.guhecloud.cube.biz.meattrade.server.pork.statistics.service;

import com.guhecloud.cube.biz.meattrade.server.pork.statistics.vo.MerchantSpotCheckStatVO;
import com.guhecloud.cube.biz.meattrade.server.third.api.ThirdMeatTradeApiService;
import com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig;
import com.guhecloud.cube.biz.meattrade.server.third.response.MerchantSpotCheckStatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantPorkStatService {

    private final ThirdMeatTradeApiService thirdMeatTradeApiService;

    /**
     * 获取商户食安统计信息
     *
     * @param merchantId
     *
     * @return
     */
    public MerchantSpotCheckStatVO getMerchantSpotCheckStat(String merchantId) {
        ThirdMeatTradeConfig requestConfig = ThirdMeatTradeConfig.getRequestConfig();
        if (Objects.nonNull(requestConfig)) {
            MerchantSpotCheckStatResponse merchantSpotCheckStat = thirdMeatTradeApiService.getMerchantSpotCheckStat(merchantId);
            return MerchantSpotCheckStatVO.builder()
                    .totalQty(merchantSpotCheckStat.getTotalQty())
                    .passQty(merchantSpotCheckStat.getPassQty())
                    .passRate(merchantSpotCheckStat.getPassRate())
                    .build();
        }
        return MerchantSpotCheckStatVO.builder().build();
    }
}
