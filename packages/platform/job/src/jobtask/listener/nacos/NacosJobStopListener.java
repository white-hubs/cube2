package com.guhecloud.platform.job.jobtask.listener.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.config.JobConfig;
import com.guhecloud.platform.job.jobtask.config.JobStopConfig;
import com.guhecloud.platform.job.jobtask.core.JobInfo;
import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.jobtask.listener.JobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NacosJobStopListener implements Listener, JobListener<JobStopConfig> {

    private final NacosConfigManager nacosConfigManager;

    private final JobScheduler jobScheduler;

    private JobStopConfig old;

    private static final ObjectMapper YAML_MAPPER;

    private static final String DATA_ID = "common-job.yaml";

    private static final String GROUP_ID = "DEFAULT_GROUP";

    static {
        YAML_MAPPER = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public NacosJobStopListener(NacosConfigManager nacosConfigManager, JobScheduler jobScheduler
                               ) {
        this.nacosConfigManager = nacosConfigManager;
        this.jobScheduler = jobScheduler;
    }

    @Override
    public Executor getExecutor() {
        return ExecutorContextHolder.getExecutor();
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        if (StringUtils.isBlank(configInfo)) {
            return;
        }
        try {
            var jobConfig = YAML_MAPPER.readValue(configInfo, JobConfig.class);
            receiveConfigInfo(jobConfig.getJob().getStop());
        } catch (JsonProcessingException e) {
            log.error("read job_config error", e);
        }
    }

    public synchronized void receiveConfigInfo(JobStopConfig jobStopConfig) {
        try {
            if (CollectionUtils.isNotEmpty(jobStopConfig.getJobNames())) {
                jobScheduler.getAllJobs().values().stream()
                        .map(JobInfo::getName)
                        .filter(name -> jobStopConfig.getJobNames().contains(name))
                        .forEach(jobScheduler::stopJob);
            }
            if (ObjectUtils.isNull(old)) {
                return;
            }
            //对比上次配置，不在此次配置内的则恢复启动
            var startJobNames = old.getJobNames().stream().filter(job -> !jobStopConfig.getJobNames().contains(job))
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(startJobNames)) {
                jobScheduler.getAllJobs().values().stream()
                        .map(JobInfo::getName)
                        .filter(startJobNames::contains)
                        .forEach(jobScheduler::startJob);
            }
        } catch (Exception e) {
            log.error("listener stop job error", e);
        } finally {
            old = jobStopConfig;
        }
    }

    @Override
    public void addListener() {
        try {
            var config = nacosConfigManager.getConfigService().getConfigAndSignListener(DATA_ID, GROUP_ID, 10000, this);
            receiveConfigInfo(config);
        } catch (NacosException e) {
            log.error("init add job stop nacos listener error.", e);
        }
    }
}
