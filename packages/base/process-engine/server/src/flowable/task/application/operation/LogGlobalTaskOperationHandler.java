package com.guhecloud.cube.base.flow.flowable.task.application.operation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogGlobalTaskOperationHandler implements GlobalTaskOperationHandler {
    @Override
    public void before() {
        System.out.println("审批通过前置处理------------------");
    }

    @Override
    public void after() {

    }
}
