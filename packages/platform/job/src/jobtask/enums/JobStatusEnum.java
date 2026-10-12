package com.guhecloud.platform.job.jobtask.enums;

/**
 * jobTask状态
 */
public enum JobStatusEnum {

    /**
     * 已启动(会执行，但是不一定是正在运行中)
     */
    STARTED,

    /**
     * 已停止(不会执行了,但是job还在，可以随时启动)
     */
    STOPPED,
}
