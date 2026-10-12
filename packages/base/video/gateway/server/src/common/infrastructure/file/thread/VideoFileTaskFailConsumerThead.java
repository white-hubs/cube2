package com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread;

import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.execute.VideoFileLocallyStoreFailExecute;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.task.VideoFileLocallyStoreTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VideoFileTaskFailConsumerThead extends Thread {

    private volatile boolean consumerThreadToStop = false;

    @Override
    public void run() {
        while (!consumerThreadToStop) {
            VideoFilePO videoFilePO = null;
            try {
                videoFilePO = VideoFileLocallyStoreFailExecute.getQueue().take();
                VideoFileLocallyStoreFailExecute.executeTask(new VideoFileLocallyStoreTask(videoFilePO));
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("consumerVideoFile Thread was interrupted during take.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void toStop() {
        this.consumerThreadToStop = true;
    }
}
