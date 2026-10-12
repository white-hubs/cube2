package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeStatisticsApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetTopSellersComparisonPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BasePageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetTopSellersComparisonPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.SellersComparisonTopStatMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.SellersComparisonTopStatPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class SellersComparisonTopStatService extends ServiceImpl<SellersComparisonTopStatMapper, SellersComparisonTopStatPO> {

    private final DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService;

    public SellersComparisonTopStatService(DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService
                                          ) {this.dbcMeatTradeStatisticsApiService = dbcMeatTradeStatisticsApiService;}

    @Transactional(rollbackFor = Exception.class)
    public Pagination<SellersComparisonTopStatPO> getSellersComparisonTopStat(StatQueryParam param) {
        GetTopSellersComparisonPageRequest request = new GetTopSellersComparisonPageRequest();
        request.setDay(param.getDay());
        request.setPageNum(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        try {
            BasePageResponse<GetTopSellersComparisonPageResponse> page = dbcMeatTradeStatisticsApiService.getTopSellersComparisonPageResponseBasePage(request);
            if (CollectionUtils.isEmpty(page.getList())) {
                return Pagination.empty();
            }
            List<SellersComparisonTopStatPO> sellersComparisonTopStatPOS = ObjectUtils.copying(page.getList(), SellersComparisonTopStatPO.class);
            this.remove(Wrappers.<SellersComparisonTopStatPO>lambdaQuery()
                                .eq(SellersComparisonTopStatPO::getTimeRange, sellersComparisonTopStatPOS.get(0).getTimeRange()));
            this.saveBatch(sellersComparisonTopStatPOS);
            return Pagination.<SellersComparisonTopStatPO>builder()
                    .total(page.getTotal().longValue())
                    .pages(page.getPages().longValue())
                    .records(sellersComparisonTopStatPOS)
                    .build();
        } catch (Exception e) {
            log.error("getSellersComparisonTopStat error", e);
        }
        return PagingUtils.convert(this.page(PagingUtils.convert(param), Wrappers.<SellersComparisonTopStatPO>lambdaQuery()
                .eq(SellersComparisonTopStatPO::getTimeRange, param.getTimeRange())));
    }
}
