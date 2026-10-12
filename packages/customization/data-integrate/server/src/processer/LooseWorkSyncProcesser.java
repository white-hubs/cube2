package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.dao.LooseWorksMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.LooseWorks;
import com.guhecloud.cube.base.dataintegrate.server.entity.ProjectReform;
import com.guhecloud.cube.base.dataintegrate.server.entity.ProjectSchedule;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.service.ProjectReformService;
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
public class LooseWorkSyncProcesser extends BaseMarketDataSyncProcesser<LooseWorks> {

    private final LooseWorksMapper looseWorksMapper;
    private final ProjectReformService projectReformService;

    public LooseWorkSyncProcesser(MarketConfigService marketConfigService, LooseWorksMapper looseWorksMapper,
                                  ProjectReformService projectReformService
                                 ) {
        super(marketConfigService);
        this.looseWorksMapper = looseWorksMapper;
        this.projectReformService = projectReformService;
    }

    @Override
    public List<LooseWorks> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("period", this.getQueryPeriod(param));
            params.add("reformPeriod", this.getQueryReformPeriod(param));
            log.info("{} 小散采集开始", param.getMarketName());
            var looseWorks = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.LOOSE_WORK.getDefaultPath()), params, getAuthorization(param), new TypeReference<LooseWorks>() {});
            if (looseWorks != null) {
                log.info("{} 小散采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                         (System.currentTimeMillis() - startTime) / 1000, looseWorks.getProjectScheduleList().size());
                return Collections.singletonList(looseWorks);
            }
        } catch (Exception e) {
            log.error("小散数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        var lastestData = looseWorksMapper.selectOne(Wrappers.<ProjectSchedule>lambdaQuery()
                                                             .eq(ProjectSchedule::getMarketTenant, param.getMarketTenant())
                                                             .orderByDesc(ProjectSchedule::getUpdateTime)
                                                             .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(lastestData) ? lastestData.getUpdateTime() : null);
    }

    private String getQueryReformPeriod(ContextParam param) {
        var lastestData = projectReformService.getOne(Wrappers.<ProjectReform>lambdaQuery()
                                                              .eq(ProjectReform::getMarketTenant, param.getMarketTenant())
                                                              .orderByDesc(ProjectReform::getCreateTime)
                                                              .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(lastestData) ? lastestData.getUpdateTime() : null);
    }

    @Override
    public List<LooseWorks> cleanData(List<LooseWorks> dataList, ContextParam param) {
        //不清洗  直接返回
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.forEach(t -> {
                for (ProjectSchedule data : t.getProjectScheduleList()) {
                    data.setMarketName(param.getMarketName());
                    data.setMarketTenant(param.getMarketTenant());
                    data.setTenant(param.getTenant());
                }
                for (ProjectReform data : t.getProjectReformList()) {
                    data.setMarketName(param.getMarketName());
                    data.setMarketTenant(param.getMarketTenant());
                    data.setTenant(param.getTenant());
                }
            });
        }
        return dataList;
    }

    @Override
    public void storeData(List<LooseWorks> dataList, ContextParam param) {
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                var looseWorks = dataList.getFirst();
                if (!CollectionUtils.isEmpty(looseWorks.getProjectScheduleList())) {
                    int batchSize = 500; // 每批500条
                    int total = looseWorks.getProjectScheduleList().size();
                    for (int i = 0; i < total; i += batchSize) {
                        int end = Math.min(i + batchSize, total);
                        List<ProjectSchedule> subList = looseWorks.getProjectScheduleList().subList(i, end);
                        looseWorksMapper.insertOrUpdateBatch(subList);
                    }
                }
                if (!CollectionUtils.isEmpty(looseWorks.getProjectReformList())) {
                    projectReformService.saveOrUpdateBatch(looseWorks.getProjectReformList());
                }
            } catch (Exception e) {
                log.error("小散数据插入失败，原因：", e);
                throw e;
            }
        }
    }
}
