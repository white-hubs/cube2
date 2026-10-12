package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeStatisticsApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetTransactionComparisonRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetTransactionComparisonResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.TransactionComparisonStatMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.TransactionComparisonStatPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionComparisonStatService extends ServiceImpl<TransactionComparisonStatMapper, TransactionComparisonStatPO> {

    private final DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService;

    public TransactionComparisonStatService(DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService
                                           ) {this.dbcMeatTradeStatisticsApiService = dbcMeatTradeStatisticsApiService;}

    public TransactionComparisonStatPO getTransactionComparisonStat(StatQueryParam param) {
        GetTransactionComparisonRequest request = new GetTransactionComparisonRequest();
        request.setDay(param.getDay());
        try {
            GetTransactionComparisonResponse response = dbcMeatTradeStatisticsApiService.getTransactionComparisonRequest(request);
            TransactionComparisonStatPO comparisonStatPO = ObjectUtils.copying(response, TransactionComparisonStatPO.class);
            this.remove(Wrappers.<TransactionComparisonStatPO>lambdaQuery()
                                .eq(TransactionComparisonStatPO::getTimeRange, response.getTimeRange()));
            this.save(comparisonStatPO);
            return comparisonStatPO;
        } catch (Exception e) {
            log.error("getTransactionComparisonStat error", e);
        }
        return this.getOne(Wrappers.<TransactionComparisonStatPO>lambdaQuery()
                                   .eq(TransactionComparisonStatPO::getTimeRange, param.getTimeRange()));
    }
}
