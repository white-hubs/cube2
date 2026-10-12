package com.guhecloud.platform.job.jobtask.bootstrap;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import com.guhecloud.platform.job.jobtask.config.JobStopConfig;
import com.guhecloud.platform.job.jobtask.listener.JobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 初始化事件监听器
 */
@Slf4j
@Component
@EventBusListen(order = 3)
public class JobInitializeListener {

    private final JobRegistrar jobRegistrar;

    private final JobListener<JobStopConfig> jobListener;

    public JobInitializeListener(JobRegistrar jobRegistrar, JobListener<JobStopConfig> jobListener) {
        this.jobRegistrar = jobRegistrar;
        this.jobListener = jobListener;
    }

    /**
     * 监听初始化事件，初始化添加job
     *
     * @param event
     */
    @Subscribe
    @AllowConcurrentEvents
    public void onApplicationEvent(SysReadEvent event) {
        log.info("job initialize listener {}", event);
        jobRegistrar.register();
        jobListener.addListener();
    }
}
