package com.guhecloud.cube.biz.emergency.server.time.core.task;

import com.guhecloud.cube.biz.emergency.server.time.core.entry.TimerTaskEntry;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Slf4j
@Data
public class TimerTaskList implements Delayed {

    /**
     * TimerTaskList 环形链表使用一个虚拟根节点root
     */
    private TimerTaskEntry root = new TimerTaskEntry(null, -1);

    {
        root.setNext(root);
        root.setPrev(root);
    }

    /**
     * bucket的过期时间
     */
    private AtomicLong expiration = new AtomicLong(-1L);

    public long getExpiration() {
        return expiration.get();
    }

    /**
     * 设置bucket的过期时间,设置成功返回true
     *
     * @param expirationMs
     *
     * @return
     */
    public boolean setExpiration(long expirationMs) {
        return expiration.getAndSet(expirationMs) != expirationMs;
    }

    public boolean addTask(TimerTaskEntry entry) {
        boolean done = false;
        while (!done) {
            //如果TimerTaskEntry已经在别的list中就先移除,同步代码块外面移除,避免死锁,一直到成功为止
            entry.remove();
            synchronized (this) {
                if (entry.getTimedTaskList() == null) {
                    //加到链表的末尾
                    entry.setTimedTaskList(this);
                    TimerTaskEntry tail = root.getPrev();
                    entry.setPrev(tail);
                    entry.setNext(root);
                    tail.setNext(entry);
                    root.setPrev(entry);
                    done = true;
                }
            }
        }
        return true;
    }

    /**
     * 从 TimedTaskList 移除指定的 timerTaskEntry
     *
     * @param entry
     */
    public void remove(TimerTaskEntry entry) {
        synchronized (this) {
            if (entry.getTimedTaskList().equals(this)) {
                entry.getNext().setPrev(entry.getPrev());
                entry.getPrev().setNext(entry.getNext());
                entry.setNext(null);
                entry.setPrev(null);
                entry.setTimedTaskList(null);
            }
        }
    }

    /**
     * 移除所有
     */
    public synchronized void clear(Consumer<TimerTaskEntry> entry) {
        TimerTaskEntry head = root.getNext();
        while (!head.equals(root)) {
            remove(head);
            entry.accept(head);
            head = root.getNext();
        }
        expiration.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        var now = LocalDateTime.now();
        long currentSeconds = DateUtils.between(now, DateUtils.beginOfDay(now), ChronoUnit.SECONDS);
        return Math.max(0, unit.convert(expiration.get() - currentSeconds, TimeUnit.SECONDS));
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof TimerTaskList) {
            return Long.compare(expiration.get(), ((TimerTaskList) o).expiration.get());
        }
        return 0;
    }
}
