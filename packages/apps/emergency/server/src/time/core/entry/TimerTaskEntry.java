package com.guhecloud.cube.biz.emergency.server.time.core.entry;

import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTask;
import com.guhecloud.cube.biz.emergency.server.time.core.task.TimerTaskList;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class TimerTaskEntry implements Comparable<TimerTaskEntry> {

    private TimerTask timerTask;
    private long expireMs;
    private TimerTaskList timedTaskList;
    private TimerTaskEntry next;
    private TimerTaskEntry prev;

    public TimerTaskEntry(TimerTask timedTask, long expireMs) {
        this.timerTask = timedTask;
        this.expireMs = expireMs;
        this.next = null;
        this.prev = null;
    }

    public void remove() {
        TimerTaskList currentList = timedTaskList;
        while (currentList != null) {
            currentList.remove(this);
            currentList = timedTaskList;
        }
    }

    @Override
    public int compareTo(TimerTaskEntry o) {
        return ((int) (this.expireMs - o.expireMs));
    }
}
