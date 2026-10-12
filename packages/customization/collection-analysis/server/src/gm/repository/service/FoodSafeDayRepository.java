package com.guhecloud.cube.biz.collection.analysis.gm.repository.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DataDictionaryEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.GmFoodSafeDayStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeDayStatisticsPO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * 食安检测
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodSafeDayRepository extends ServiceImpl<GmFoodSafeDayStatisticsMapper, FoodSafeDayStatisticsPO> {

    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 昨日食安检测统计(某段时间统计)
     *
     * @param bgnTime  开始时间（yyyy-MM-dd HH:mm:ss）
     * @param endTime  结束时间（yyyy-MM-dd HH:mm:ss）
     * @param tenant   租户
     * @param nowTime  当前时间
     * @param operator 操作人
     * @param userId   操作人id
     * @param userType 操作人类型
     *
     * @return 是否成功
     */
    public Boolean foodSafePeriodTimePull(String bgnTime, String endTime, Integer tenant, LocalDateTime nowTime, String operator,
                                          String userId, ActorTypeEnum userType
                                         ) {
        Map<String, Object> map = this.getHttpMap(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name(), DataDictionaryEnum.PARK_FOOD_SAFE_DAY.name(), bgnTime, endTime);
        if (map == null) {
            return false;
        }
        FoodSafeDayStatisticsPO po = JsonUtils.deserialize(map.get("data"), FoodSafeDayStatisticsPO.class);
        if (po == null) {
            return false;
        }
        var dayTime = DateUtils.beginOfDay(DateUtils.parse(bgnTime));
        po.setTimeDay(dayTime);
        po.setTimeDayFormat(DateUtils.formatDate(dayTime));
        po.setTenant(tenant);
        EntityAuditor.populateCreateAudit(po, operator, userId, userType, nowTime);
        this.save(po);
        this.remove(Wrappers.<FoodSafeDayStatisticsPO>lambdaQuery().eq(FoodSafeDayStatisticsPO::getTimeDayFormat, po.getTimeDayFormat())
                            .lt(FoodSafeDayStatisticsPO::getCreateTime, DateUtils.offset(nowTime, ChronoUnit.SECONDS, -1)));
        return Boolean.TRUE;
    }

    private Map<String, Object> getHttpMap(String itemCode, String code, String bgnTime, String endTime) {
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode(itemCode);
        param.setCode(code);
        DataDictionaryDTO ex = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
        if (ex == null) {
            return null;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bgnTime", bgnTime);
        paramMap.put("endTime", endTime);
        String result = HttpUtil.post(ex.getVariable(), JSON.toJSONString(paramMap));
        log.info("（code）result:{}", result);
        return JsonUtils.deserialize(result, Map.class);
    }
}
