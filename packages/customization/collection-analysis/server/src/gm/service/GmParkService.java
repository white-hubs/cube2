package com.guhecloud.cube.biz.collection.analysis.gm.service;

import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportWeekDTO;
import com.guhecloud.platform.base.model.Period;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:45
 * @Description:
 * @Version: 1.0
 */
public interface GmParkService {
    /**
     * 昨日来货统计采集
     */
    void parkSettleCategoryDay(List<DataDictionaryDTO> dataDictionaryDTOList, Integer tenant);

    /**
     * 近一周来货趋势图
     */
    void parkSettleCategoryWeek(List<DataDictionaryDTO> dataDictionaryDTOList);

    void transportDay(List<DataDictionaryDTO> param);

    void transportWeek(List<DataDictionaryDTO> param);

    ParkSettleCategoryDayDTO getGoodsYesterday(LocalDateTime deliveryDate);

    List<ParkSettleCategoryWeekDTO> getGoodsWeek(Period period);

    TransportDayDTO getTransportYesterday(LocalDateTime deliveryDate);

    List<TransportWeekDTO> getTransportWeek(Period period);
}
