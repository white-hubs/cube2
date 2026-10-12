package com.guhecloud.cube.biz.collection.analysis.gm.collection;

import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportWeekDTO;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:47
 * @Description:
 * @Version: 1.0
 */
public interface GmCollectionService {
    /**
     * 1、昨日来货重量  2、昨日来货量品类占比
     */
    ParkSettleCategoryDayDTO getParkSettleCategoryDay(List<DataDictionaryDTO> param, String deliveryDate);

    /**
     * 近一周来货趋势图
     */
    List<ParkSettleCategoryWeekDTO> parkSettleCategoryWeek(List<DataDictionaryDTO> param, String bgnTime, String endTime);

    /**
     * 昨日转运统计
     */
    TransportDayDTO transportDay(List<DataDictionaryDTO> param, String bgnTime, String endTime);

    List<TransportWeekDTO> transportWeek(List<DataDictionaryDTO> param, String bgnTime, String endTime);

}
