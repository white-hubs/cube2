package com.guhecloud.cube.base.flow.flowable.process.infrastructure.config;

import com.guhecloud.cube.base.flow.flowable.process.infrastructure.result.ProcessResultHandleDispatcher;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.result.ProcessResultHandler;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/15 11:33
 * @description
 */
@Configuration
public class ProcessConfiguration implements ApplicationRunner {
    @Autowired
    private List<ProcessResultHandler> processResultHandlers;

    public void init() {
        if (CollectionUtils.isNotEmpty(processResultHandlers)) {
            processResultHandlers.forEach(processResultHandler -> ProcessResultHandleDispatcher.getInstance()
                    .registerListener(processResultHandler));
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }
}
