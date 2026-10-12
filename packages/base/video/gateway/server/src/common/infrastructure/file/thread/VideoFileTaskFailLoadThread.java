package com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread;

import com.guhecloud.cube.base.videogw.server.common.application.VideoFileService;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoFileStatusEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.execute.VideoFileLocallyStoreFailExecute;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class VideoFileTaskFailLoadThread extends Thread {

    private static final Integer TRANSFER_NUMBER = 8;

    private final VideoFileService videoFileService;

    private volatile boolean scheduleThreadToStop = false;

    public VideoFileTaskFailLoadThread(VideoFileService videoFileService) {
        this.videoFileService = videoFileService;
    }

    @Override
    public void run() {
        while (!scheduleThreadToStop) {
            long now = System.currentTimeMillis();
            Date retryTime = new Date(now);
            try {
                if (VideoFileLocallyStoreFailExecute.queueIsEmpty()) {
                    List<VideoFilePO> localStayStorageFiles = this.videoFileService.getLocalStorageFailFiles(100, TRANSFER_NUMBER, retryTime);
                    for (VideoFilePO videoFilePO : localStayStorageFiles) {
                        Boolean addSuccess = VideoFileLocallyStoreFailExecute.addQueueTask(videoFilePO);
                        if (addSuccess) {
                            //修改状态
                            videoFilePO.setLocallyStoredStatus(VideoFileStatusEnum.LOCAL_PROCESS_STORAGE.name());
                        }
                    }
                }
            } catch (MyBatisSystemException e) {
                log.error("数据库查询转存失败的文件数据失败", e);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("getVideoLocalStorageFailFiles Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void toStop() {
        scheduleThreadToStop = true;
    }
}
