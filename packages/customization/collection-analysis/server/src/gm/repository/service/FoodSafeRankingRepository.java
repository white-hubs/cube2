package com.guhecloud.cube.biz.collection.analysis.gm.repository.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DataDictionaryEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.FoodSafeRankingStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeRankingStatisticsPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodSafeRankingRepository extends ServiceImpl<FoodSafeRankingStatisticsMapper, FoodSafeRankingStatisticsPO> {

    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 食安风险排名
     *
     * @param bgnTime  开始时间（yyyy-MM-dd HH:mm:ss）
     * @param endTime  结束时间（yyyy-MM-dd HH:mm:ss）
     * @param tenant   租户
     * @param nowTime  当前时间
     * @param operator 操作人
     *
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean foodSafeRankingPull(String bgnTime, String endTime, Integer tenant, LocalDateTime nowTime, String operator,
                                       String userId, ActorTypeEnum userType
                                      ) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bgnTime", bgnTime);
        paramMap.put("endTime", endTime);
        Map<String, Object> map = this.getHttpMap(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name(), DataDictionaryEnum.PARK_RISK__RANKING.name(), paramMap);
        if (map == null) {
            return false;
        }
        List<FoodSafeRankingStatisticsPO> list = JsonUtils.deserialize(map.get("data"), new TypeReference<List<FoodSafeRankingStatisticsPO>>() {});
        if (list == null) {
            return false;
        }
        if (list.isEmpty()) {
            return true;
        }
        List<String> delteDayList = new ArrayList<>();
        var dayTime = DateUtils.beginOfDay(nowTime);
        list.forEach(po -> {
            po.setTimeDay(dayTime);
            po.setTimeDayFormat(DateUtils.formatDate(dayTime));
            po.setTimePeriod(bgnTime + "," + endTime);
            po.setTenant(tenant);
            EntityAuditor.populateCreateAudit(po, operator, userId, userType, nowTime);
            delteDayList.add(po.getTimePeriod());
        });
        if (!CollectionUtils.isEmpty(list)) {
            this.saveBatch(list);
        }
        if (!CollectionUtils.isEmpty(delteDayList)) {
            this.remove(Wrappers.<FoodSafeRankingStatisticsPO>lambdaQuery().in(FoodSafeRankingStatisticsPO::getTimePeriod, delteDayList)
                                .lt(FoodSafeRankingStatisticsPO::getCreateTime, DateUtils.offset(nowTime, ChronoUnit.SECONDS, -1)));
        }
        return true;
    }

    private Map<String, Object> getHttpMap(String itemCode, String code, Map<String, Object> paramMap) {
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode(itemCode);
        param.setCode(code);
        DataDictionaryDTO ex = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
        if (ex == null) {
            return null;
        }
        String result = HttpUtil.post(ex.getVariable(), paramMap == null ? null : JSON.toJSONString(paramMap));
        log.info("（code）result:{}", result);
        return JsonUtils.deserialize(result, Map.class);
    }
}
