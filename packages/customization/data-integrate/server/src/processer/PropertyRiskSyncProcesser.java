package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.dao.PropertyFilingMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.PropertyFiling;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资产风险数据采集
 */
@Slf4j
@Service
public class PropertyRiskSyncProcesser extends BaseMarketDataSyncProcesser<PropertyFiling> {

    private final PropertyFilingMapper propertyFilingMapper;

    public PropertyRiskSyncProcesser(MarketConfigService marketConfigService,
                                     PropertyFilingMapper propertyFilingMapper) {
        super(marketConfigService);
        this.propertyFilingMapper = propertyFilingMapper;
    }

    @Override
    public List<PropertyFiling> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("period", this.getQueryPeriod(param));
            log.info("{} 资产风险采集开始", param.getMarketName());
            var dataList = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.PROPERTY_RISK.getDefaultPath()), params, getAuthorization(param), new TypeReference<List<PropertyFiling>>() {});
            if (CollectionUtils.isNotEmpty(dataList)) {
                log.info("{} 资产风险采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                         (System.currentTimeMillis() - startTime) / 1000, dataList.size());
                return dataList;
            }
        } catch (Exception e) {
            log.error("资产风险数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        log.error("资产风险数据参数：{}", JsonUtils.serialize(param));
        var lastestData = propertyFilingMapper.selectOne(Wrappers.<PropertyFiling>lambdaQuery()
                                                                 .eq(PropertyFiling::getMarketTenant, param.getMarketTenant())
                                                                 .orderByDesc(PropertyFiling::getUpdateTime)
                                                                 .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(lastestData) ? lastestData.getUpdateTime() : null);
    }

    @Override
    public List<PropertyFiling> cleanData(List<PropertyFiling> dataList, ContextParam param) {
        //不清洗  直接返回
        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.stream().map(data -> {
                if (data != null && data.getId() != null) {
                    data.setMarketName(param.getMarketName());
                    data.setMarketTenant(param.getMarketTenant());
                    data.setTenant(param.getTenant());
                    return data;
                }
                return null;
            }).filter(data -> data != null && data.getId() != null).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void storeData(List<PropertyFiling> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                int batchSize = 500; // 每批500条
                int total = dataList.size();
                for (int i = 0; i < total; i += batchSize) {
                    int end = Math.min(i + batchSize, total);
                    List<PropertyFiling> subList = dataList.subList(i, end);
                    propertyFilingMapper.insertOrUpdateBatch(subList);
                }
            } catch (Exception e) {
                log.error("资产风险数据插入失败，原因：", e);
                throw e;
            }
        }
    }
}
