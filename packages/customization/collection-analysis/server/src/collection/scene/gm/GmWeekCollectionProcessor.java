package com.guhecloud.cube.biz.collection.analysis.collection.scene.gm;

import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.biz.collection.analysis.collection.scene.BaseSceneCollectionProcessor;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.CollectionSceneEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.FoodSafeWeekRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.VehicleWeekRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.service.GmParkService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 周统计
 */
@Slf4j
@Component
public class GmWeekCollectionProcessor extends BaseSceneCollectionProcessor<String, String> {
    @Autowired
    private FoodSafeWeekRepository foodSafeWeekRepository;
    @Autowired
    private VehicleWeekRepository vehicleWeekRepository;
    @Autowired
    private GmParkService gmParkService;
    @Autowired
    private DataDictionaryAdapter dataDictionaryAdapter;

    public GmWeekCollectionProcessor() {
        super(CollectionSceneEnum.GM_WEEK_COLLECTION.name());
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
        String startTime = DateUtils.format(DateUtils.offsetDay(yesterdayStartTime, -7));
        String endTime = DateUtils.format(DateUtils.endOfDay(yesterdayStartTime));
        String operator = "job";

        DataDictionaryaramParam param = new DataDictionaryaramParam();
        param.setItemCode(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name());
        List<DataDictionaryDTO> dictionaryList = dataDictionaryAdapter.getDataDictionaryByItem(param);
        // 食安周统计
        foodSafeWeekRepository.foodSafeWeekPull(tenant, nowTime, operator);
        // 车辆周统计
        vehicleWeekRepository.VehicleWeekPull(startTime, endTime, tenant, nowTime, operator);
        // 车辆装载统计
        // 来货周统计
        gmParkService.parkSettleCategoryWeek(dictionaryList);
        // 转运周统计
        gmParkService.transportWeek(dictionaryList);
        return Collections.emptyList();
    }

}
