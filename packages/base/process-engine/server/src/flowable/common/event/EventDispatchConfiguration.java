package com.guhecloud.cube.base.flow.flowable.common.event;

import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/15 10:20
 * @description
 */
@Configuration
public class EventDispatchConfiguration implements ApplicationRunner {
    @Autowired
    private List<EventListener> eventListeners;

    public void init() {
        if (CollectionUtils.isNotEmpty(eventListeners)) {
            eventListeners.forEach(eventListener -> EventDispatcher.getInstance().registerListener(eventListener));
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }
}
