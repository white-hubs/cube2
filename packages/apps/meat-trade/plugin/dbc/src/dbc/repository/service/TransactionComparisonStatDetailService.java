package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeStatisticsApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetTransactionComparisonPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BasePageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetTransactionComparisonPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.TransactionComparisonStatDetailMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.TransactionComparisonStatDetailPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TransactionComparisonStatDetailService extends ServiceImpl<TransactionComparisonStatDetailMapper, TransactionComparisonStatDetailPO> {

    private final DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService;

    public TransactionComparisonStatDetailService(DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService
                                                 ) {this.dbcMeatTradeStatisticsApiService = dbcMeatTradeStatisticsApiService;}

    @Transactional(rollbackFor = Exception.class)
    public List<TransactionComparisonStatDetailPO> getTransactionComparisonStatDetails(StatQueryParam param) {
        GetTransactionComparisonPageRequest request = new GetTransactionComparisonPageRequest();
        request.setDay(param.getDay());
        request.setPageNum(1);
        request.setPageSize(param.getDay());
        try {
            BasePageResponse<GetTransactionComparisonPageResponse> page = dbcMeatTradeStatisticsApiService.getTransactionComparisonPage(request);
            List<TransactionComparisonStatDetailPO> transactionComparisonStatDetailPOS = ObjectUtils.copying(page.getList(), TransactionComparisonStatDetailPO.class);
            this.remove(Wrappers.<TransactionComparisonStatDetailPO>lambdaQuery()
                                .eq(TransactionComparisonStatDetailPO::getTimeRange, transactionComparisonStatDetailPOS.get(0)
                                        .getTimeRange()));
            this.saveBatch(transactionComparisonStatDetailPOS);
            return transactionComparisonStatDetailPOS;
        } catch (Exception e) {
            log.error("getTransactionComparisonStatDetails error", e);
        }
        return this.list(Wrappers.<TransactionComparisonStatDetailPO>lambdaQuery()
                                 .eq(TransactionComparisonStatDetailPO::getTimeRange, param.getTimeRange())
                                 .orderByDesc(TransactionComparisonStatDetailPO::getStartDate));
    }
}
