package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.DbcMeatTradeStatisticsApiService;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetMeatTestingPageRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics.GetMeatTestingRankingRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.BasePageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatTestingPageResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics.GetMeatTestingRankingResponse;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.param.StatQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.mapper.MeatTestingRankingStatMapper;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po.MeatTestingRankingStatPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class MeatTestingRankingStatService extends ServiceImpl<MeatTestingRankingStatMapper, MeatTestingRankingStatPO> {

    private final DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService;

    public MeatTestingRankingStatService(DbcMeatTradeStatisticsApiService dbcMeatTradeStatisticsApiService
                                        ) {this.dbcMeatTradeStatisticsApiService = dbcMeatTradeStatisticsApiService;}

    @Transactional(rollbackFor = Exception.class)
    public List<MeatTestingRankingStatPO> getMeatTestingRankingTest(StatQueryParam param) {
        GetMeatTestingRankingRequest request = new GetMeatTestingRankingRequest();
        request.setDay(param.getDay());
        try {
            List<GetMeatTestingRankingResponse> meatTestingRankingList = dbcMeatTradeStatisticsApiService.getMeatTestingRankingList(request);
            if (CollectionUtils.isEmpty(meatTestingRankingList)) {
                return Collections.emptyList();
            }
            this.remove(Wrappers.<MeatTestingRankingStatPO>lambdaQuery()
                                .eq(MeatTestingRankingStatPO::getTimeRange, meatTestingRankingList.get(0).getTimeRange()));
            List<MeatTestingRankingStatPO> list = ObjectUtils.copying(meatTestingRankingList, MeatTestingRankingStatPO.class);
            this.saveBatch(list);
            return list;
        } catch (Exception e) {
            log.error("getMeatTestingRankingList error", e);
        }
        return this.list(Wrappers.<MeatTestingRankingStatPO>lambdaQuery()
                                 .eq(MeatTestingRankingStatPO::getTimeRange, param.getTimeRange()));
    }

    public Pagination<GetMeatTestingPageResponse> getMeatTestingRankingPage(StatQueryParam param) {
        try {
            GetMeatTestingPageRequest request = new GetMeatTestingPageRequest();
            request.setPageNum(param.getPageNumber());
            request.setPageSize(param.getPageSize());
            request.setStartDate(DateUtils.format(param.getPeriod().getBegin()));
            request.setEndDate(DateUtils.format(param.getPeriod().getEnd()));
            request.setMeatTestingItemCode(param.getItemCode());
            BasePageResponse<GetMeatTestingPageResponse> page = dbcMeatTradeStatisticsApiService.getMeatTestingRankingPage(request);
            if (CollectionUtils.isEmpty(page.getList())) {
                return Pagination.empty();
            }
            return Pagination.<GetMeatTestingPageResponse>builder()
                    .total(page.getTotal().longValue())
                    .pages(page.getPages().longValue())
                    .records(page.getList())
                    .build();
        } catch (Exception e) {
            log.error("getMeatTestingRankingList error, itemName:{}", param.getItemCode(), e);
        }
        return Pagination.empty();
    }
}
