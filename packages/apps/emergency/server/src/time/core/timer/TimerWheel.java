package com.guhecloud.cube.biz.emergency.server.time.core.timer;

import com.guhecloud.cube.biz.emergency.server.time.core.entry.TimerTaskEntry;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTaskList;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.DelayQueue;

@Slf4j
public class TimerWheel {

    /**
     * 一个槽的时间间隔(时间轮最小刻度)
     */
    private long tickMs;

    /**
     * 时间轮大小(槽的个数)
     */
    private int wheelSize;

    /**
     * 一轮的时间跨度
     */
    private long interval;

    /**
     * 槽
     */
    private TimerTaskList[] buckets;

    /**
     * 一个timer只有一个delayqueue
     */
    private DelayQueue<TimerTaskList> delayQueue;

    public TimerTaskList[] getBuckets() {
        return buckets;
    }

    public TimerWheel(long tickMs, int wheelSize, DelayQueue<TimerTaskList> delayQueue) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.buckets = new TimerTaskList[wheelSize];
        this.delayQueue = delayQueue;
        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new TimerTaskList();
        }
    }

    public boolean add(TimerTaskEntry entry) {
        long expiration = entry.getExpireMs();
        //如果执行时间不在当天
        var now = DateUtils.now();
        if (entry.getTimerTask().getExecuteTime() < DateUtils.toTimestamp(DateUtils.beginOfDay(now)) ||
            DateUtils.toTimestamp(DateUtils.endOfDay(now)) < entry.getTimerTask().getExecuteTime()) {
            return true;
        }
        long currentSeconds = DateUtils.between(DateUtils.beginOfDay(now), now, ChronoUnit.SECONDS);
        if (expiration < tickMs + currentSeconds) {
            //到期了
            return false;
        } else if (expiration < currentSeconds + interval) {
            //扔进当前时间轮的某个槽里,只有时间大于某个槽,才会放进去
            long virtualId = (expiration / tickMs);
            int index = (int) (virtualId % wheelSize);
            TimerTaskList bucket = buckets[index];
            bucket.addTask(entry);
            //设置bucket 过期时间
            if (bucket.setExpiration(virtualId * tickMs)) {
                //设好过期时间的bucket需要入队
                delayQueue.offer(bucket);
            }
            return true;
        }
        return false;
    }

    /**
     * 推进指针
     *
     * @param timestamp
     */
    public void advanceLock(long timestamp) {

    }
}
