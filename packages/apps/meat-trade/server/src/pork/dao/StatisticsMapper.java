package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.dto.StatisticsDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Statistics;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsMpQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("meattrade-statisticsMapper")
public interface StatisticsMapper extends BaseMapper<Statistics> {

    int insertBatch(List<Statistics> statistics);

    StatisticsDTO queryAll();

    Page<StatisticsDTO> pageStatistics(Page<?> page, @Param("param") StatisticsQueryParam queryParam);

    Page<StatisticsDTO> pageStatisticsV2(Page<?> page, @Param("param") StatisticsQueryParam queryParam);

    List<StatisticsDTO> selectStatisticsV2(@Param("param") StatisticsQueryParam queryParam);

    Page<StatisticsDTO> selectPageGoodsCategoryStatistics(Page<?> page, @Param("param") StatisticsQueryParam queryParam);

    List<StatisticsDTO> selectGoodsCategoryStatistics(@Param("param") StatisticsQueryParam queryParam);

    Page<StatisticsDTO> saleStatistics(Page<?> page, @Param("param") StatisticsQueryParam queryParam);

    Page<StatisticsDTO> saleGoodsCategoryStatistics(Page<?> page, @Param("param") StatisticsQueryParam queryParam);

    Page<StatisticsDTO> pageMpStatistics(Page<?> page, @Param("param") StatisticsMpQueryParam queryParam);
}
