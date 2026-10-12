package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeStatisticsApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetSellerSupplyStatsSumRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetSellerSupplyStatsSumResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.SellerSupplySumStatMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.SellerSupplySumStatPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SellerSupplySumStatService extends ServiceImpl<SellerSupplySumStatMapper, SellerSupplySumStatPO> {

    private final DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService;

    public SellerSupplySumStatService(DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService
                                     ) {this.dbcMeatTradeStatisticsApiService = dbcMeatTradeStatisticsApiService;}

    @Transactional(rollbackFor = Exception.class)
    public SellerSupplySumStatPO getSellerSupplySumStat(StatQueryParam param) {
        GetSellerSupplyStatsSumRequest request = new GetSellerSupplyStatsSumRequest();
        request.setDay(param.getDay());
        try {
            GetSellerSupplyStatsSumResponse response = dbcMeatTradeStatisticsApiService.getSellerSupplyStatsSum(request);
            SellerSupplySumStatPO supplySumStatPO = ObjectUtils.copying(response, SellerSupplySumStatPO.class);
            this.remove(Wrappers.<SellerSupplySumStatPO>lambdaQuery()
                                .eq(SellerSupplySumStatPO::getTimeRange, response.getTimeRange()));
            this.save(supplySumStatPO);
            return supplySumStatPO;
        } catch (Exception e) {
            log.error("getSellerSupplySumStat error", e);
        }
        return this.getOne(Wrappers.<SellerSupplySumStatPO>lambdaQuery()
                                   .eq(SellerSupplySumStatPO::getTimeRange, param.getTimeRange()));
    }
}
