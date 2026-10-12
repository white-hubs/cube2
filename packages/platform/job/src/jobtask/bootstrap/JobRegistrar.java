package com.guhecloud.platform.job.jobtask.bootstrap;

import com.guhecloud.platform.job.jobtask.core.JobScheduler;
import com.guhecloud.platform.job.jobtask.dto.JobAddInfo;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JobRegistrar {

    private final JobScanner jobScanner;

    private final JobScheduler jobScheduler;

    private volatile boolean registered = false;

    public JobRegistrar(JobScanner jobScanner, JobScheduler jobScheduler) {
        this.jobScanner = jobScanner;
        this.jobScheduler = jobScheduler;
    }

    /**
     * 注册任务
     */
    public synchronized void register() {
        if (registered) {
            return;
        }
        List<JobAddInfo> jobAddInfos = jobScanner.scan();
        log.info("starting to register {} jobs", jobAddInfos.size());
        for (JobAddInfo jobAddInfo : jobAddInfos) {
            try {
                jobScheduler.addJob(jobAddInfo);
                log.info("job register successful job {}", jobAddInfo.getName());
            } catch (Exception e) {
                log.error("job register failed job {}", jobAddInfo.getName(), e);
            }
        }
        registered = true;
        log.info("jobs registration completed");
    }

    @PreDestroy
    public void stop() {
        jobScheduler.getAllJobs().keySet().forEach(jobScheduler::stopJob);
        registered = false;
    }
}
