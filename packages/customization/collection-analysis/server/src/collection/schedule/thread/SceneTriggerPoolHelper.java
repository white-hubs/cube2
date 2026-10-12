package com.guhecloud.cube.biz.collection.analysis.collection.schedule.thread;

import com.guhecloud.cube.biz.collection.analysis.collection.schedule.dto.ScheduleJob;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * job trigger thread pool helper
 */
@Slf4j
public class SceneTriggerPoolHelper {

    private ThreadPoolExecutor triggerPool = null;

    public void start() {
        triggerPool = new ThreadPoolExecutor(
                10,
                30,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "sceneTriggerPoolHelper-triggerPool-" + r.hashCode());
                    }
                });
    }

    public void stop() {
        triggerPool.shutdownNow();
        log.info("scene_trigger thread pool shutdown success.");
    }

    /**
     * add trigger
     */
    public void addTrigger(final ScheduleJob job
                          ) {
        triggerPool.execute(() -> {
            try {
                SessionContextHolder.requestTenant(job.getTenant());
                List<?> collectionData = job.getCollectionProcessor().getCollectionData();
                job.getCollectionProcessor().cleanData(collectionData);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    // ---------------------- helper ----------------------

    private final static SceneTriggerPoolHelper helper = new SceneTriggerPoolHelper();

    public static void toStart() {
        helper.start();
    }

    public static void toStop() {
        helper.stop();
    }

    /**
     * @param scheduleJob 调度任务
     */
    public static void trigger(ScheduleJob<?, ?> scheduleJob
                              ) {
        helper.addTrigger(scheduleJob);
    }

}
