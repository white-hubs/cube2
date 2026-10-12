package com.guhecloud.platform.job.jobtask.config;

import lombok.Data;

@Data
public class JobConfig {

    /**
     * job配置
     */
    private Job job;

    @Data
    public static class Job {

        /**
         * 调度配置
         */
        private JobSchedulerConfig scheduler;

        /**
         * 停止配置
         */
        private JobStopConfig stop;
    }
}
