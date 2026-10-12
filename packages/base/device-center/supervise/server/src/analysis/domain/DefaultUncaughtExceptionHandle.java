package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pengjian
 * @date 2022/8/1 10:29
 * @description
 */
@Slf4j
public class DefaultUncaughtExceptionHandle implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("任务异常:{}", e.getMessage());
    }
}
