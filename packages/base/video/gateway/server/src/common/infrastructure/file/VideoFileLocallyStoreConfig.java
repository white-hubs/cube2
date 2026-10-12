package com.guhecloud.cube.base.videogw.server.common.infrastructure.file;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.base.videogw.server.common.application.VideoFileService;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread.VideoFileTaskConsumerThead;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread.VideoFileTaskFailConsumerThead;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread.VideoFileTaskFailLoadThread;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread.VideoFileTaskLoadThread;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EventBusListen(order = 1)
public class VideoFileLocallyStoreConfig implements DisposableBean {

    private VideoFileTaskLoadThread videoFileLoadThread = null;

    private VideoFileTaskConsumerThead videoFileConsumerThread = null;

    private VideoFileTaskFailLoadThread videoFileFailLoadThread = null;

    private VideoFileTaskFailConsumerThead videoFileTaskFailConsumerThead;

    @Getter
    private final VideoFileService videoFileService;

    public VideoFileLocallyStoreConfig(VideoFileService videoFileService) {this.videoFileService = videoFileService;}

    @Subscribe
    @AllowConcurrentEvents
    public void afterPropertiesSet(SysReadEvent event) {
        videoFileLoadThread = new VideoFileTaskLoadThread(getVideoFileService());
        videoFileLoadThread.setDaemon(true);
        videoFileLoadThread.setName("video gw,videoFileLoadThread");
        videoFileLoadThread.start();

        videoFileConsumerThread = new VideoFileTaskConsumerThead();
        videoFileConsumerThread.setDaemon(true);
        videoFileConsumerThread.setName("video gw,videoFileConsumerThread");
        videoFileConsumerThread.start();

        //重试的线程
        videoFileFailLoadThread = new VideoFileTaskFailLoadThread(getVideoFileService());
        videoFileFailLoadThread.setDaemon(true);
        videoFileFailLoadThread.setName("video gw,videoFileStoreFail Retry LoadThread");
        videoFileFailLoadThread.start();

        videoFileTaskFailConsumerThead = new VideoFileTaskFailConsumerThead();
        videoFileTaskFailConsumerThead.setDaemon(true);
        videoFileTaskFailConsumerThead.setName("video gw,videoFileTaskFail RetryConsumerThread");
        videoFileTaskFailConsumerThead.start();
    }

    @Override
    public void destroy() throws Exception {
        videoFileLoadThread.toStop();
        stopThread(videoFileLoadThread);
        videoFileConsumerThread.toStop();
        stopThread(videoFileConsumerThread);

        //失败重试线程退出
        videoFileFailLoadThread.toStop();
        stopThread(videoFileFailLoadThread);
        videoFileTaskFailConsumerThead.toStop();
        stopThread(videoFileTaskFailConsumerThead);
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
}
