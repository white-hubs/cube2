package com.guhecloud.cube.biz.collection.analysis.collection.scene.gm;

import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.collection.analysis.collection.scene.BaseSceneCollectionProcessor;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.CollectionSceneEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.FoodSafeDayRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.FoodSafeRankingRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.VehicleDayStatisticsRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.service.GmParkService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 日统计
 */
@Slf4j
@Component
public class GmDayCollectionProcessor extends BaseSceneCollectionProcessor<String, String> {
    @Autowired
    private FoodSafeDayRepository foodSafeDayRepository;
    @Autowired
    private FoodSafeRankingRepository foodSafeRankingRepository;
    @Autowired
    private VehicleDayStatisticsRepository vehicleDayStatisticsRepository;
    @Autowired
    private GmParkService gmParkService;
    @Autowired
    private DataDictionaryAdapter dataDictionaryAdapter;

    public GmDayCollectionProcessor() {
        super(CollectionSceneEnum.GM_DAY_COLLECTION.name());
    }

    @Override
    public List<String> cleanData(List<String> strings) {
        return strings;
    }

    @Override
    public List<String> getCollectionData() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        var nowTime = LocalDateTime.now();
        var yesterdayStartTime = DateUtils.beginOfDay(DateUtils.offsetDay(nowTime, -1));
        String startTime = DateUtils.format(yesterdayStartTime);
        String endTime = DateUtils.format(DateUtils.endOfDay(yesterdayStartTime));
        String operator = "job";

        DataDictionaryaramParam param = new DataDictionaryaramParam();
        param.setItemCode(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name());
        List<DataDictionaryDTO> dictionaryList = dataDictionaryAdapter.getDataDictionaryByItem(param);
        // 食安每日统计
        foodSafeDayRepository.foodSafePeriodTimePull(startTime, endTime, tenant, nowTime, operator, StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM);
        // 食安风险排名
        foodSafeRankingRepository.foodSafeRankingPull(DateUtils.format(DateUtils.offsetDay(yesterdayStartTime, -30)), DateUtils.format(yesterdayStartTime),
                                                      tenant, nowTime, operator, StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM);
        // 车辆每日车流
        vehicleDayStatisticsRepository.VehicleDayPull(DateUtils.formatDate(yesterdayStartTime), tenant, nowTime, operator, StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM);
        // 来货昨日统计
        gmParkService.parkSettleCategoryDay(dictionaryList, tenant);
        // 转运昨日统计
        gmParkService.transportDay(dictionaryList);
        return Collections.emptyList();
    }

}
