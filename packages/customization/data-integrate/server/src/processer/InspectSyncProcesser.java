package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.dao.InspectTaskMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyInspectTask;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
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
public class InspectSyncProcesser extends BaseMarketDataSyncProcesser<SafetyInspectTask> {

    private final InspectTaskMapper inspectTaskMapper;

    public InspectSyncProcesser(MarketConfigService marketConfigService, InspectTaskMapper inspectTaskMapper) {
        super(marketConfigService);
        this.inspectTaskMapper = inspectTaskMapper;
    }

    @Override
    public List<SafetyInspectTask> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("period", this.getQueryPeriod(param));
            log.info("{} 巡检采集开始", param.getMarketName());
            var dataList = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.INSPECT.getDefaultPath()), params, getAuthorization(param), new TypeReference<List<SafetyInspectTask>>() {});
            if (CollectionUtils.isNotEmpty(dataList)) {
                log.info("{} 巡检采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                         (System.currentTimeMillis() - startTime) / 1000, dataList.size());
                return dataList;
            }
        } catch (Exception e) {
            log.error("巡检任务数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        var inspectTask = inspectTaskMapper.selectOne(Wrappers.<SafetyInspectTask>lambdaQuery()
                                                              .eq(SafetyInspectTask::getMarketTenant, param.getMarketTenant())
                                                              .orderByDesc(SafetyInspectTask::getUpdateTime)
                                                              .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(inspectTask) ? inspectTask.getUpdateTime() : null);
    }

    @Override
    public List<SafetyInspectTask> cleanData(List<SafetyInspectTask> dataList, ContextParam param) {
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
    public void storeData(List<SafetyInspectTask> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                int batchSize = 500; // 每批500条
                int total = dataList.size();
                for (int i = 0; i < total; i += batchSize) {
                    int end = Math.min(i + batchSize, total);
                    List<SafetyInspectTask> subList = dataList.subList(i, end);
                    inspectTaskMapper.insertOrUpdateInspectBatch(subList);
                }
            } catch (Exception e) {
                log.error("巡检数据插入失败，原因：", e);
                throw e;
            }
        }
    }
}
