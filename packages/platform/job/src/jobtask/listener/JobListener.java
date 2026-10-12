package com.guhecloud.platform.job.jobtask.listener;

public interface JobListener<T> {

    void addListener();

    void receiveConfigInfo(T newT);
}
