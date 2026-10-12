package com.guhecloud.cube.base.videogw.server.common.infrastructure.file.task;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.videogw.server.common.application.VideoFileService;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoFileStatusEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.facade.file.service.VideogwFileService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.requester.sdk.client.VideoClient;
import com.guhecloud.subsystem.requester.sdk.vo.video.VideoAccessibleSnapCaptureVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class VideoFileLocallyStoreTask implements Runnable {

    private final VideoFilePO videoFilePO;

    public VideoFileLocallyStoreTask(VideoFilePO videoFilePO) {this.videoFilePO = videoFilePO;}

    @Override
    public void run() {
        SessionContextHolder.clearSessionContext();
        SessionContextHolder.requestTenant(videoFilePO.getTenant());
        VideoClient videoClient = ApplicationContextHolder.getBean(VideoClient.class);
        VideogwFileService videogwFileService = ApplicationContextHolder.getBean(VideogwFileService.class);
        VideoFileService videoFileService = ApplicationContextHolder.getBean(VideoFileService.class);
        VideoAccessibleSnapCaptureVO accessibleSnapCaptureByUrl = videoClient.getAccessibleSnapCaptureByUrl(videoFilePO.getPath());
        if (Objects.nonNull(accessibleSnapCaptureByUrl)) {
            FileVO fileVO = videogwFileService.uploadFileByPath(videoFilePO.getFileId(), accessibleSnapCaptureByUrl.getCaptureUrl(), videoFilePO.getStoreSceneCode());
            if (Objects.nonNull(fileVO)) {
                videoFilePO.setPath(fileVO.getPath());
                videoFilePO.setSize(fileVO.getSize());
                videoFilePO.setLocallyStoredStatus(VideoFileStatusEnum.LOCAL_SUCCESS_STORAGE.name());
                videoFilePO.setType(fileVO.getType());
                videoFileService.updateById(videoFilePO);
                return;
            }
        }
        videoFilePO.setLocallyStoredStatus(VideoFileStatusEnum.LOCAL_FAILED_STORAGE.name());
        videoFilePO.setRetryTimes(Optional.ofNullable(videoFilePO.getRetryTimes()).orElse(0) + 1);
        videoFilePO.setRetryNextTime(new Date(System.currentTimeMillis() + calculateDelay(videoFilePO.getRetryTimes())));
        videoFileService.updateById(videoFilePO);
    }

    private long calculateDelay(int retryCount) {
        return (long) Math.pow(2, retryCount) * 5000;
    }
}
