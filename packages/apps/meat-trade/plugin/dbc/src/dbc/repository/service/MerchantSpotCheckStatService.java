package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.MerchantSpotCheckStatisticsResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.MerchantSpotCheckStatMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.MerchantSpotCheckStatPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantSpotCheckStatService extends ServiceImpl<MerchantSpotCheckStatMapper, MerchantSpotCheckStatPO> {

    private final DbcMeatTradeApiService dbcMeatTradeApiService;

    public MerchantSpotCheckStatPO getMerchantSpotCheckStat(String merchantId) {
        MerchantSpotCheckStatPO merchantSpotCheckStatPO = getMerchantSpotCheckStatByMerchantId(merchantId);
        try {
            MerchantSpotCheckStatisticsResponse spotCheckStatistics = this.dbcMeatTradeApiService.getMerchantSpotCheckStatistics(merchantId);
            if (Objects.nonNull(merchantSpotCheckStatPO)) {
                merchantSpotCheckStatPO.setTotalQty(spotCheckStatistics.getTotalQty());
                merchantSpotCheckStatPO.setPassQty(spotCheckStatistics.getPassQty());
                merchantSpotCheckStatPO.setPassRate(spotCheckStatistics.getPassRate());
                this.baseMapper.updateById(merchantSpotCheckStatPO);
            } else {
                merchantSpotCheckStatPO = new MerchantSpotCheckStatPO();
                merchantSpotCheckStatPO.setMerchantId(merchantId);
                merchantSpotCheckStatPO.setTotalQty(spotCheckStatistics.getTotalQty());
                merchantSpotCheckStatPO.setPassQty(spotCheckStatistics.getPassQty());
                merchantSpotCheckStatPO.setPassRate(spotCheckStatistics.getPassRate());
                this.baseMapper.insert(merchantSpotCheckStatPO);
            }
            return merchantSpotCheckStatPO;
        } catch (Exception e) {
            log.error("获取商户:{},食安抽检统计数据失败:", merchantId, e);
        }
        return merchantSpotCheckStatPO;
    }

    public MerchantSpotCheckStatPO getMerchantSpotCheckStatByMerchantId(String merchantId) {
        List<MerchantSpotCheckStatPO> merchantSpotCheckStatPOS = this.baseMapper.selectList(Wrappers.<MerchantSpotCheckStatPO>lambdaQuery()
                                                                                                    .eq(MerchantSpotCheckStatPO::getMerchantId, merchantId)
                                                                                           );
        if (CollectionUtils.isEmpty(merchantSpotCheckStatPOS)) {
            return null;
        }
        return merchantSpotCheckStatPOS.get(0);
    }
}
