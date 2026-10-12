package com.guhecloud.platform.job.jobtask.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "job.stop")
public class JobStopConfig {

    /**
     * 根据job名称停止
     */
    private Set<String> jobNames;

    public Set<String> getJobNames() {
        if (jobNames == null) {
            return Collections.emptySet();
        }
        return jobNames;
    }
}
