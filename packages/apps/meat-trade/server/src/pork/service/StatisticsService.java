package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.dto.StatisticsDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Statistics;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsMpQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatisticsQueryParam;

public interface StatisticsService extends IService<Statistics> {

    /**
     * 统计商家的白条猪理货和销售数据
     *
     * @param queryParam
     *
     * @return
     */
    Page<StatisticsDTO> pageStatisticsV2(StatisticsQueryParam queryParam);

    /**
     * 统计商家商品品类理货和消息数据
     *
     * @param queryParam
     *
     * @return
     */
    Page<StatisticsDTO> pageGoodsCategoryStatistics(StatisticsQueryParam queryParam);

    Page<StatisticsDTO> pageMpStatistics(StatisticsMpQueryParam queryParam);

}
