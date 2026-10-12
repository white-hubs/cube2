package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.dao.ExamTaskMapper;
import com.guhecloud.cube.base.dataintegrate.server.dao.SafetyExamTaskPaperMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyExamTask;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyExamTaskPaper;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExamSyncProcesser extends BaseMarketDataSyncProcesser<SafetyExamTask> {

    private final ExamTaskMapper examTaskMapper;

    private final SafetyExamTaskPaperMapper safetyExamTaskPaperMapper;

    public ExamSyncProcesser(MarketConfigService marketConfigService, ExamTaskMapper examTaskMapper,
                             SafetyExamTaskPaperMapper safetyExamTaskPaperMapper
                            ) {
        super(marketConfigService);
        this.examTaskMapper = examTaskMapper;
        this.safetyExamTaskPaperMapper = safetyExamTaskPaperMapper;
    }

    @Override
    public List<SafetyExamTask> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("period", this.getQueryPeriod(param));
            log.info("{} 考试采集开始", param.getMarketName());
            var dataList = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.EXAM.getDefaultPath()), params, getAuthorization(param), new TypeReference<List<SafetyExamTask>>() {});
            if (CollectionUtils.isNotEmpty(dataList)) {
                log.info("{} 考试采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                         (System.currentTimeMillis() - startTime) / 1000, dataList.size());
                return dataList;
            }
        } catch (Exception e) {
            log.error("考试数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        var lastestData = examTaskMapper.selectOne(Wrappers.<SafetyExamTask>lambdaQuery()
                                                           .eq(SafetyExamTask::getMarketTenant, param.getMarketTenant())
                                                           .orderByDesc(SafetyExamTask::getUpdateTime)
                                                           .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(lastestData) ? lastestData.getUpdateTime() : null);
    }

    @Override
    public List<SafetyExamTask> cleanData(List<SafetyExamTask> dataList, ContextParam param) {
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
    public void storeData(List<SafetyExamTask> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                var batchSize = 500; // 每批500条
                for (var safetyExamTasks : ListUtils.partition(dataList, batchSize)) {
                    examTaskMapper.insertOrUpdateBatch(safetyExamTasks);
                    //先删除
                    var taskIds = safetyExamTasks.stream().map(SafetyExamTask::getId).collect(Collectors.toSet());
                    safetyExamTaskPaperMapper.delete(Wrappers.<SafetyExamTaskPaper>lambdaQuery()
                                                             .in(SafetyExamTaskPaper::getExamTaskId, taskIds));
                }
                List<SafetyExamTaskPaper> safetyExamTaskPapers = new ArrayList<>();
                //处理
                for (var examTask : dataList) {
                    if (CollectionUtils.isEmpty(examTask.getExamPapers())) {
                        return;
                    }
                    var examTaskPapers = examTask.getExamPapers().stream()
                            .map(examPaper -> SafetyExamTaskPaper.builder()
                                    .id(IdWorker.getId())
                                    .examTaskId(examTask.getId())
                                    .examPaperId(examPaper.id())
                                    .examPaperName(examPaper.name())
                                    .build()).collect(Collectors.toSet());
                    safetyExamTaskPapers.addAll(examTaskPapers);
                }
                if (!CollectionUtils.isEmpty(safetyExamTaskPapers)) {
                    for (var papers : ListUtils.partition(safetyExamTaskPapers, batchSize)) {
                        safetyExamTaskPaperMapper.insertOrUpdateBatch(papers);
                    }
                }
            } catch (Exception e) {
                log.error("考试数据插入失败，原因：", e);
            }
        }
    }
}
