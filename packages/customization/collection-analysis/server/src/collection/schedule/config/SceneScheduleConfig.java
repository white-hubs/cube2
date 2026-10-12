package com.guhecloud.cube.biz.collection.analysis.collection.schedule.config;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.biz.collection.analysis.collection.scene.SceneCollectionProcessFactory;
import com.guhecloud.cube.biz.collection.analysis.collection.schedule.thread.SceneJobLoadThread;
import com.guhecloud.cube.biz.collection.analysis.collection.schedule.thread.SceneTriggerPoolHelper;
import com.guhecloud.cube.biz.collection.analysis.repository.service.SceneScheduleJobService;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EventBusListen(order = 1)
public class SceneScheduleConfig implements InitializingBean, DisposableBean {

    private SceneJobLoadThread sceneJobLoadThread = null;

    @Getter
    private final SceneScheduleJobService sceneScheduleJobService;

    @Getter
    private final SceneCollectionProcessFactory sceneCollectionProcessFactory;

    public SceneScheduleConfig(SceneScheduleJobService sceneScheduleJobService, SceneCollectionProcessFactory sceneCollectionProcessFactory
                              ) {
        this.sceneScheduleJobService = sceneScheduleJobService;
        this.sceneCollectionProcessFactory = sceneCollectionProcessFactory;
    }

    @Override
    public void afterPropertiesSet() {
        sceneJobLoadThread = new SceneJobLoadThread(getSceneCollectionProcessFactory(), getSceneScheduleJobService());
        sceneJobLoadThread.setDaemon(true);
        sceneJobLoadThread.setName("video gw,videoFileLoadThread");
    }

    @Override
    public void destroy() throws Exception {
        sceneJobLoadThread.setToStop();
        stopThread(sceneJobLoadThread);

        SceneTriggerPoolHelper.toStop();
    }

    private void stopThread(Thread thread) {
        try {
            TimeUnit.SECONDS.sleep(1);  // wait
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        if (thread.getState() != Thread.State.TERMINATED) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    private void initStart(SysReadEvent event) {
        sceneJobLoadThread.start();
        SceneTriggerPoolHelper.toStart();
    }
}
