package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyInspectRecordItem;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.service.SafetyInspectRecordItemService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class InspectRecordItemSyncProcesses extends BaseMarketDataSyncProcesser<SafetyInspectRecordItem> {

    private final SafetyInspectRecordItemService safetyInspectRecordItemService;

    public InspectRecordItemSyncProcesses(MarketConfigService marketConfigService,
                                          SafetyInspectRecordItemService safetyInspectRecordItemService
                                            ) {
        super(marketConfigService);
        this.safetyInspectRecordItemService = safetyInspectRecordItemService;
    }

    @Override
    public List<SafetyInspectRecordItem> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("period", this.getQueryPeriod(param));
            log.info("{} 巡检采集开始", param.getMarketName());
            var dataList = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.ABNORMAL_TAG_RECORD_ITEM.getDefaultPath()), params, getAuthorization(param), new TypeReference<List<SafetyInspectRecordItem>>() {});
            if (CollectionUtils.isNotEmpty(dataList)) {
                log.info("{} 巡检采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                         (System.currentTimeMillis() - startTime) / 1000, dataList.size());
                return dataList;
            }
        } catch (Exception e) {
            log.error("报警数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        var inspectTask = safetyInspectRecordItemService.getOne(Wrappers.<SafetyInspectRecordItem>lambdaQuery()
                                                                        .eq(SafetyInspectRecordItem::getMarketTenant, param.getMarketTenant())
                                                                        .orderByDesc(SafetyInspectRecordItem::getUpdateTime)
                                                                        .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(inspectTask) ? inspectTask.getUpdateTime() : null);
    }

    @Override
    public List<SafetyInspectRecordItem> cleanData(List<SafetyInspectRecordItem> dataList, ContextParam param) {
        //不清洗  直接返回
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.parallelStream().forEach(data -> {
                data.setMarketName(param.getMarketName());
                data.setMarketTenant(param.getMarketTenant());
                data.setTenant(param.getTenant());
            });
        }
        return dataList;
    }

    @Override
    public void storeData(List<SafetyInspectRecordItem> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                safetyInspectRecordItemService.saveOrUpdateBatch(dataList);
            } catch (Exception e) {
                log.error("巡检数据插入失败，原因：", e);
                throw e;
            }
        }
    }
}
