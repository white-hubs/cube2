package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeStatisticsApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetMeatFarmSupplyRankingPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BasePageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatFarmSupplyRankingPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.MeatFarmSupplyRankingStatMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.MeatFarmSupplyRankingStatPO;
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
public class MeatFarmSupplyRankingStatService extends ServiceImpl<MeatFarmSupplyRankingStatMapper, MeatFarmSupplyRankingStatPO> {

    private final DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService;

    public MeatFarmSupplyRankingStatService(DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService
                                           ) {this.dbcMeatTradeStatisticsApiService = dbcMeatTradeStatisticsApiService;}

    @Transactional(rollbackFor = Exception.class)
    public Pagination<MeatFarmSupplyRankingStatPO> getMeatFarmSupplyRankingStatList(StatQueryParam param) {
        GetMeatFarmSupplyRankingPageRequest request = new GetMeatFarmSupplyRankingPageRequest();
        request.setDay(param.getDay());
        request.setPageNum(param.getPageNumber());
        request.setPageSize(param.getPageSize());
        try {
            BasePageResponse<GetMeatFarmSupplyRankingPageResponse> page = dbcMeatTradeStatisticsApiService.getMeatFarmSupplyRankingPageResponseBasePage(request);
            if (CollectionUtils.isEmpty(page.getList())) {
                return Pagination.empty();
            }
            List<MeatFarmSupplyRankingStatPO> meatFarmSupplyRankingStatPOS = ObjectUtils.copying(page.getList(), MeatFarmSupplyRankingStatPO.class);
            this.remove(Wrappers.<MeatFarmSupplyRankingStatPO>lambdaQuery()
                                .eq(MeatFarmSupplyRankingStatPO::getTimeRange, meatFarmSupplyRankingStatPOS.get(0).getTimeRange()));
            this.saveBatch(meatFarmSupplyRankingStatPOS);
            return Pagination.<MeatFarmSupplyRankingStatPO>builder()
                    .total(page.getTotal().longValue())
                    .pages(page.getPages().longValue())
                    .records(meatFarmSupplyRankingStatPOS)
                    .build();
        } catch (Exception e) {
            log.error("getMeatFarmSupplyRankingStatList error", e);
        }
        return PagingUtils.convert(this.page(PagingUtils.convert(param), Wrappers.<MeatFarmSupplyRankingStatPO>lambdaQuery()
                .eq(MeatFarmSupplyRankingStatPO::getTimeRange, param.getTimeRange())));
    }

}
