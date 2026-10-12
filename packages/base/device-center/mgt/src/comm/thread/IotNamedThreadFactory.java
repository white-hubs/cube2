package com.guhecloud.cube.iot.mgt.comm.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class IotNamedThreadFactory implements ThreadFactory {

    protected final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    protected final AtomicInteger mThreadNum = new AtomicInteger(1);

    protected final String mPrefix;

    protected final boolean mDaemon;

    protected final ThreadGroup mGroup;

    public IotNamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public IotNamedThreadFactory(String prefix, boolean daemon) {
        mPrefix = prefix + "-td-";
        mDaemon = daemon;
        mGroup = Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemon);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return mGroup;
    }
}
