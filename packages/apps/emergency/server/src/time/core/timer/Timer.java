package com.guhecloud.cube.biz.emergency.server.time.core.timer;

import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;

public interface Timer {

    void start();

    /**
     * 添加一个新任务
     *
     * @param timerTask
     */
    void add(TimerTask timerTask);

    /**
     * 移除任务
     *
     * @param id
     */
    void remove(Long id);

    /**
     * 推动指针
     *
     * @param timeout
     */
    void advanceClock(long timeout);

    /**
     * 等待执行的任务
     *
     * @return
     */
    int size();

    /**
     * 关闭服务,剩下的无法被执行
     */
    void shutdown();
}
