package com.guhecloud.cube.biz.emergency.server.time.core.timer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.cube.biz.emergency.server.time.core.entry.TimerTaskEntry;
import com.guhecloud.cube.biz.emergency.server.time.core.plugin.ExecuteExpirePlugin;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTaskList;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RuleTimer implements Timer {

    private static RuleTimer instance = new RuleTimer();

    public static RuleTimer getInstance() {
        return instance;
    }

    /**
     * 底层时间轮
     */
    private TimerWheel timeWheel;
    /**
     * 一个Timer只有一个延时队列
     */
    private DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();

    /**
     * 执行过期策略插件
     */
    private ExecuteExpirePlugin executeExpirePlugin;

    /**
     * 过期任务执行线程
     */
    private ExecutorService workerThreadPool;
    /**
     * 轮询delayQueue获取过期任务线程
     */
    private ExecutorService bossThreadPool;

    public RuleTimer() {
        this.timeWheel = new TimerWheel(1, 24 * 60 * 60, delayQueue);
        this.workerThreadPool = Executors.newFixedThreadPool(5,
                                                             new ThreadFactoryBuilder().setNameFormat("emergencyTimeJob-pool-%d").build());
        this.bossThreadPool = Executors.newFixedThreadPool(1);
        this.executeExpirePlugin = new ExecuteExpirePlugin();
    }

    public void addTimerTaskEntry(TimerTaskEntry entry) {
        //如果执行时间在当天
        if (!timeWheel.add(entry) || !executeExpirePlugin.reissueTask(entry.getTimerTask())) {
            //已经过期了
            TimerTask timerTask = entry.getTimerTask();
            log.info("=====任务:{} 已到期,准备执行============", timerTask.getId());
            workerThreadPool.submit(timerTask);
        }
    }

    @Override
    public void start() {
        //1s推动一次时间轮运转
        this.bossThreadPool.submit(() -> {
            while (true) {
                this.advanceClock(1000);
            }
        });
    }

    @Override
    public void add(TimerTask timerTask) {
        log.info("=======添加规则任务开始====task:{}", timerTask.getId());
        TimerTaskEntry entry = new TimerTaskEntry(timerTask, timerTask.getDelayMs());
        timerTask.setTimerTaskEntry(entry);
        addTimerTaskEntry(entry);
    }

    @Override
    public void remove(Long id) {
        for (TimerTaskList timerTaskList : timeWheel.getBuckets()) {
            synchronized (timerTaskList) {
                TimerTaskEntry entry = timerTaskList.getRoot().getNext();
                while (entry != timerTaskList.getRoot()) {
                    if (entry.getTimerTask().getId().equals(id)) {
                        timerTaskList.remove(entry);
                        entry.setTimedTaskList(null);
                        if (delayQueue.remove(timerTaskList)) {
                            log.info("移除延迟任务成功");
                        } else {
                            log.info("移除延迟任务失败");
                        }
                        return; // 找到匹配的任务并成功移除后，结束方法
                    }
                    entry = entry.getNext();
                }
            }
        }
    }

    /**
     * 推动指针运转获取过期任务
     *
     * @param timeout 时间间隔
     *
     * @return
     */
    @Override
    public synchronized void advanceClock(long timeout) {
        try {
            if (!delayQueue.isEmpty()) {
                TimerTaskList bucket = delayQueue.poll(timeout, TimeUnit.SECONDS);
                if (bucket != null) {
                    //推进时间
                    timeWheel.advanceLock(bucket.getExpiration());
                    //执行过期任务(包含降级)
                    bucket.clear(this::addTimerTaskEntry);
                }
            }
        } catch (InterruptedException e) {
            log.error("advanceClock error");
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void shutdown() {
        this.bossThreadPool.shutdown();
        this.workerThreadPool.shutdown();
        this.timeWheel = null;
    }
}
