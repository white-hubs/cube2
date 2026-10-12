package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.StatisticsMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.dto.StatisticsDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Statistics;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsMpQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.StatisticsService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("meattrade-statisticsService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {

    @Override
    public Page<StatisticsDTO> pageStatisticsV2(StatisticsQueryParam queryParam) {
        if (queryParam.getPeriod() != null) {
            queryParam.setStartTime(queryParam.getPeriod().getBegin());
            queryParam.setEndTime(queryParam.getPeriod().getEnd());
        }
        Page<StatisticsDTO> statisticsDTOPage = this.baseMapper.saleStatistics(PagingUtils.convert(queryParam), queryParam);
        //不存在交易信息时,构建理货信息返回即可
        if (CollectionUtils.isEmpty(statisticsDTOPage.getRecords())) {
            //理货信息
            Page<StatisticsDTO> arrangeStatisticsDTOPage = this.baseMapper.pageStatisticsV2(PagingUtils.convert(queryParam), queryParam);
            return arrangeStatisticsDTOPage;
        }
        queryParam.setSupplierIds(statisticsDTOPage.getRecords().stream().map(StatisticsDTO::getSupplierId).collect(Collectors.toList()));
        //理货信息
        List<StatisticsDTO> statisticsDTO = this.baseMapper.selectStatisticsV2(queryParam);
        extentPorkArrangeStatisticsInfo(statisticsDTOPage, statisticsDTO);
        return statisticsDTOPage;
    }

    @Override
    public Page<StatisticsDTO> pageGoodsCategoryStatistics(StatisticsQueryParam queryParam) {
        if (queryParam.getPeriod() != null) {
            queryParam.setStartTime(queryParam.getPeriod().getBegin());
            queryParam.setEndTime(queryParam.getPeriod().getEnd());
        }
        Page<StatisticsDTO> statisticsDTOPage = this.baseMapper.saleGoodsCategoryStatistics(PagingUtils.convert(queryParam), queryParam);
        if (CollectionUtils.isEmpty(statisticsDTOPage.getRecords())) {
            Page<StatisticsDTO> arrangeStatisticsDTOPage = this.baseMapper.selectPageGoodsCategoryStatistics(PagingUtils.convert(queryParam), queryParam);
            return arrangeStatisticsDTOPage;
        }
        queryParam.setSupplierIds(statisticsDTOPage.getRecords().stream().map(StatisticsDTO::getSupplierId).collect(Collectors.toList()));
        List<StatisticsDTO> statisticsDTOS = this.baseMapper.selectGoodsCategoryStatistics(queryParam);
        extentPorkArrangeStatisticsInfo(statisticsDTOPage, statisticsDTOS);
        return statisticsDTOPage;
    }

    private void extentPorkArrangeStatisticsInfo(Page<StatisticsDTO> statisticsDTOPage, List<StatisticsDTO> statisticsDTOS) {
        if (!CollectionUtils.isEmpty(statisticsDTOS)) {
            Map<String, List<StatisticsDTO>> saleSt = statisticsDTOS.stream()
                    .collect(Collectors.groupingBy(statisticsDTO -> statisticsDTO.getSupplierId() + "-" +
                                                                    statisticsDTO.getGoodsCategoryId()));
            statisticsDTOPage.getRecords().stream().forEach(s -> {
                List<StatisticsDTO> statisticsDTOList = saleSt.get(s.getSupplierId() + "-" + s.getGoodsCategoryId());
                if (!CollectionUtils.isEmpty(statisticsDTOList)) {
                    StatisticsDTO statisticsDTO = statisticsDTOList.get(0);
                    s.setPorkArrangeSlices(statisticsDTO.getPorkArrangeSlices());
                    s.setPorkArrangeWeight(statisticsDTO.getPorkArrangeWeight());
                    s.setActualAmount(s.getAmount() - s.getCommission());
                    s.setAveragePrice(BigDecimal.valueOf(s.getAmount())
                                              .divide(BigDecimal.valueOf(s.getDealWeight()), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            });
        }
    }

    @Override
    public Page<StatisticsDTO> pageMpStatistics(@NonNull StatisticsMpQueryParam queryParam) {
        return this.baseMapper.pageMpStatistics(PagingUtils.convert(queryParam), queryParam);
    }
}
