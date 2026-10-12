package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.dataintegrate.server.dao.SafetyRiskIndexMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyRiskIndex;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 安全风险得分数据采集
 *
 * @author lanchun
 * @date 2024/4/2 14:20
 */
@Slf4j
@Service
public class SafetyRiskIndexSyncProcesser extends BaseMarketDataSyncProcesser<SafetyRiskIndex> {

    private final SafetyRiskIndexMapper safetyRiskIndexMapper;

    public SafetyRiskIndexSyncProcesser(MarketConfigService marketConfigService, SafetyRiskIndexMapper safetyRiskIndexMapper) {
        super(marketConfigService);
        this.safetyRiskIndexMapper = safetyRiskIndexMapper;
    }

    @Override
    public List<SafetyRiskIndex> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            var lastWeek = DateUtils.lastWeek();
            var beginOfWeek = DateUtils.beginOfWeek(lastWeek);
            var endOfWeek = DateUtils.endOfWeek(lastWeek);
            var period = DateUtils.formatDate(beginOfWeek) + "," + DateUtils.formatDate(endOfWeek);
            var formatDateTime = DateUtils.format(beginOfWeek) + "," + DateUtils.format(endOfWeek);
            params.add("period", period);
            log.info("{}安全风险指数采集开始", param.getMarketName());
            JsonNode jsonNode = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.SAFETY_RISK_INDEX.getDefaultPath()), params, getAuthorization(param), new TypeReference<>() {});
            log.info("{} 安全风险指数采集时间：{}秒", param.getMarketName(), (System.currentTimeMillis() - startTime) / 1000);
            if (Objects.nonNull(jsonNode)) {
                List<SafetyRiskIndex> safetyRiskIndices = new ArrayList<>();
                var safetyRiskIndex = SafetyRiskIndex.builder()
                        .riskIndex(JsonUtils.getNodeValue(jsonNode, "riskIndex", Integer.class))
                        .statisticalDate(formatDateTime)
                        .statisticalPeriod(getCurrentPeriod())
                        .riskIndexDetailJson(JsonUtils.serialize(jsonNode))
                        .build();
                safetyRiskIndices.add(safetyRiskIndex);
                return safetyRiskIndices;
            }
        } catch (Exception e) {
            log.error("安全风险指数得分数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    /**
     * @Author wql
     * @Description 获取当前周期
     * @Date 9:44 2023/10/17
     **/
    private String getCurrentPeriod() {
        var now = LocalDateTime.now();
        var year = now.getYear();
        var week = DateUtils.getWeekOfYear(now);
        return String.valueOf(year) + week;
    }

    @Override
    public List<SafetyRiskIndex> cleanData(List<SafetyRiskIndex> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.forEach(safetyRiskIndex -> {
                safetyRiskIndex.setMarketName(param.getMarketName());
                safetyRiskIndex.setMarketTenant(param.getMarketTenant());
                safetyRiskIndex.setTenant(param.getTenant());
                safetyRiskIndex.setDeleted(0L);
                safetyRiskIndex.setCreateBy("system");
                safetyRiskIndex.setCreatorType(ActorTypeEnum.SYSTEM);
                safetyRiskIndex.setCreateTime(LocalDateTime.now());
                safetyRiskIndex.setUpdateBy("system");
                safetyRiskIndex.setUpdaterType(ActorTypeEnum.SYSTEM);
                safetyRiskIndex.setUpdateTime(LocalDateTime.now());
            });
        }
        return dataList;
    }

    @Override
    public void storeData(List<SafetyRiskIndex> dataList, ContextParam param) {
        long startTime = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (SafetyRiskIndex safetyRiskIndex : dataList) {
                try {
                    SafetyRiskIndex riskIndex = this.safetyRiskIndexMapper.selectOne(Wrappers.<SafetyRiskIndex>lambdaQuery()
                                                                                             .eq(SafetyRiskIndex::getStatisticalDate, safetyRiskIndex.getStatisticalDate())
                                                                                             .eq(SafetyRiskIndex::getMarketTenant, safetyRiskIndex.getMarketTenant())
                                                                                             .eq(SafetyRiskIndex::getTenant, safetyRiskIndex.getTenant()));
                    if (Objects.isNull(riskIndex)) {
                        this.safetyRiskIndexMapper.insert(safetyRiskIndex);
                    } else {
                        riskIndex.setRiskIndex(safetyRiskIndex.getRiskIndex());
                        riskIndex.setRiskIndexDetailJson(safetyRiskIndex.getRiskIndexDetailJson());
                        this.safetyRiskIndexMapper.updateById(riskIndex);
                    }
                } catch (Exception e) {
                    log.error("市场{},安全风险指数得分插入失败，原因：", param.getMarketName(), e);
                    throw e;
                }
            }
        }
        log.info("{} 安全风险指数得分插入时间：{}", param.getMarketName(), (System.currentTimeMillis() - startTime) / 1000);
    }
}
