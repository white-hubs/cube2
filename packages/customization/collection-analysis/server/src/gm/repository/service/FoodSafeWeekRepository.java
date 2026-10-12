package com.guhecloud.cube.biz.collection.analysis.gm.repository.service;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DataDictionaryEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.DictionaryItemEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.FoodSafeWeekStatisticsMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.FoodSafeWeekStatisticsPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodSafeWeekRepository extends ServiceImpl<FoodSafeWeekStatisticsMapper, FoodSafeWeekStatisticsPO> {
    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 近一周食安检测次数
     *
     * @param tenant   租户
     * @param nowTime  当前时间
     * @param operator 操作人
     *
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean foodSafeWeekPull(Integer tenant, LocalDateTime nowTime, String operator) {
        List<String> delteDayList = new ArrayList<>();
        List<FoodSafeWeekStatisticsPO> list = this.getPOListByThird();
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        list.forEach(po -> {
            var timeDay = DateUtils.beginOfDay(DateUtils.parseDate(po.getTesteddate()));
            po.setTimeDay(timeDay);
            po.setTimeDayFormat(DateUtils.formatDate(timeDay));
            po.setTenant(tenant);
            EntityAuditor.populateCreateAudit(po, operator, StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, nowTime);
            delteDayList.add(po.getTimeDayFormat());
        });
        if (!CollectionUtils.isEmpty(list)) {
            this.saveBatch(list);
        }
        if (!CollectionUtils.isEmpty(delteDayList)) {
            this.remove(Wrappers.<FoodSafeWeekStatisticsPO>lambdaQuery().in(FoodSafeWeekStatisticsPO::getTimeDayFormat, delteDayList)
                                .lt(FoodSafeWeekStatisticsPO::getCreateTime, DateUtils.offset(nowTime, ChronoUnit.SECONDS, -1)));
        }
        return true;
    }

    public List<FoodSafeWeekStatisticsPO> getPOListByThird() {
        Map<String, Object> map;
        try {
            map = this.getHttpMap(DictionaryItemEnum.GM_VISUALIZATION_DATA_URL.name(), DataDictionaryEnum.ONLINE_SECURITY_CHECK.name());
        } catch (Exception e) {
            log.error("光明食安周统计获取失败：{0}", e);
            return Collections.emptyList();
        }
        if (map == null) {
            return Collections.emptyList();
        }
        Object resultObj = map.get("result");
        if (resultObj == null) {
            return Collections.emptyList();
        }
        Object dataObj = ((LinkedHashMap<?, ?>) resultObj).get("data");
        if (dataObj == null) {
            return Collections.emptyList();
        }
        Object recordsObj = ((LinkedHashMap<?, ?>) dataObj).get("records");
        if (recordsObj == null) {
            return Collections.emptyList();
        }

        return JsonUtils.deserialize(recordsObj, new TypeReference<List<FoodSafeWeekStatisticsPO>>() {});
    }

    private Map<String, Object> getHttpMap(String itemCode, String code) {
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode(itemCode);
        param.setCode(code);
        DataDictionaryDTO ex = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
        if (ex == null) {
            return null;
        }

        String result = HttpUtil.get(ex.getVariable());
        log.info("（code）result:{}", result);
        return JsonUtils.deserialize(result, Map.class);
    }
}
