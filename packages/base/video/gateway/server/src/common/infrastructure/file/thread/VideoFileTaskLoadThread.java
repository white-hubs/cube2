package com.guhecloud.cube.base.videogw.server.common.infrastructure.file.thread;

import com.guhecloud.cube.base.videogw.server.common.application.VideoFileService;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoFileStatusEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.file.execute.VideoFileLocallyStoreExecute;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class VideoFileTaskLoadThread extends Thread {

    private final VideoFileService videoFileService;

    private volatile boolean scheduleThreadToStop = false;

    public VideoFileTaskLoadThread(VideoFileService videoFileService) {
        this.videoFileService = videoFileService;
    }

    @Override
    public void run() {
        while (!scheduleThreadToStop) {
            try {
                if (VideoFileLocallyStoreExecute.queueIsEmpty()) {
                    List<VideoFilePO> localStayStorageFiles = this.videoFileService.getLocalStayStorageFiles(100);
                    for (VideoFilePO videoFilePO : localStayStorageFiles) {
                        Boolean addSuccess = VideoFileLocallyStoreExecute.addQueueTask(videoFilePO);
                        if (addSuccess) {
                            //修改状态
                            videoFilePO.setLocallyStoredStatus(VideoFileStatusEnum.LOCAL_PROCESS_STORAGE.name());
                        }
                    }
                }
            } catch (MyBatisSystemException e) {
                log.error("数据库查询待转存的文件数据失败", e);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("getVideoLocalStayStorageFiles Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void toStop() {
        scheduleThreadToStop = true;
    }
}
