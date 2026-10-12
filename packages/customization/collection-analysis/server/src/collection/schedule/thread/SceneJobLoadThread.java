package com.guhecloud.cube.biz.collection.analysis.collection.schedule.thread;

import com.guhecloud.cube.biz.collection.analysis.collection.scene.BaseSceneCollectionProcessor;
import com.guhecloud.cube.biz.collection.analysis.collection.scene.SceneCollectionProcessFactory;
import com.guhecloud.cube.biz.collection.analysis.collection.schedule.dto.ScheduleJob;
import com.guhecloud.cube.biz.collection.analysis.collection.schedule.utils.CronExpression;
import com.guhecloud.cube.biz.collection.analysis.repository.po.SceneScheduleJobPO;
import com.guhecloud.cube.biz.collection.analysis.repository.service.SceneScheduleJobService;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SceneJobLoadThread extends Thread {

    private volatile boolean toStop = false;

    private final SceneCollectionProcessFactory sceneCollectionProcessFactory;

    private final SceneScheduleJobService sceneScheduleJobService;
    // 最大可执行的过期时长(1小时)
    private static final long PRE_READ_MS = 60 * 60 * 1000;

    public SceneJobLoadThread(SceneCollectionProcessFactory sceneCollectionProcessFactory, SceneScheduleJobService sceneScheduleJobService
                             ) {
        this.sceneCollectionProcessFactory = sceneCollectionProcessFactory;
        this.sceneScheduleJobService = sceneScheduleJobService;
    }

    @Override
    public void run() {
        while (!toStop) {
            try {
                TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                if (!toStop) {
                    log.error(e.getMessage(), e);
                }
            }
            try {
                long now = System.currentTimeMillis();
                List<SceneScheduleJobPO> jobs = this.sceneScheduleJobService.getTriggerJobs(now);
                for (SceneScheduleJobPO job : jobs) {
                    if (now > job.getNextTriggerTime() + PRE_READ_MS) {
                        //超过规定时间(间隔太长)不触发
                        log.warn("job, schedule misfire, jobId = {} ", job.getId());
                        //刷新下次执行时间
                        refreshNextValidTime(job, LocalDateTime.now());
                    } else if (now > job.getNextTriggerTime()) {
                        // 1、trigger
                        BaseSceneCollectionProcessor<?, ?> sceneCollectionProcessor = sceneCollectionProcessFactory.getBaseSceneCollectionProcessor(job.getCode());
                        SceneTriggerPoolHelper.trigger(new ScheduleJob<>(job.getId(), job.getTenant(), sceneCollectionProcessor));
                        log.debug("job, schedule push trigger : jobId = {} ", job.getId());
                        // 2、fresh next
                        refreshNextValidTime(job, LocalDateTime.now());
                    }
                    this.sceneScheduleJobService.updateById(job);
                }
            } catch (Exception e) {
                if (!toStop) {
                    log.error("sceneJobLodThread error:", e);
                }
            }
        }
    }

    private void refreshNextValidTime(SceneScheduleJobPO jobInfo, LocalDateTime fromTime) throws Exception {
        LocalDateTime nextValidTime = generateNextValidTime(jobInfo, fromTime);
        if (nextValidTime != null) {
            jobInfo.setLastTriggerTime(jobInfo.getNextTriggerTime());
            jobInfo.setNextTriggerTime(DateUtils.toTimestamp(nextValidTime));
        } else {
            jobInfo.setLastTriggerTime(0L);
            jobInfo.setNextTriggerTime(0L);
            log.warn("refreshNextValidTime fail for job: jobId={}, cron={}",
                     jobInfo.getId(), jobInfo.getCron());
        }
    }

    public LocalDateTime generateNextValidTime(SceneScheduleJobPO jobInfo, LocalDateTime fromTime) throws Exception {
        return new CronExpression(jobInfo.getCron()).getNextValidTimeAfter(fromTime);
    }

    public void setToStop() {
        toStop = true;
    }
}
