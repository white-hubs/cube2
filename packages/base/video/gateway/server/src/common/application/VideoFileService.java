package com.guhecloud.cube.base.videogw.server.common.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.videogw.server.common.domain.enumeration.VideoFileStatusEnum;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.mapper.VideoFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class VideoFileService extends ServiceImpl<VideoFileMapper, VideoFilePO> {

    public List<VideoFilePO> getLocalStayStorageFiles(Integer maxSize) {
        Page<VideoFilePO> videoFilePOPage = this.baseMapper.selectPage(new Page<>(1, maxSize), Wrappers.<VideoFilePO>lambdaQuery()
                                                                               .eq(VideoFilePO::getLocallyStored, Boolean.TRUE)
                                                                               .eq(VideoFilePO::getLocallyStoredStatus, VideoFileStatusEnum.LOCAL_STAY_STORAGE)
                                                                      );
        if (CollectionUtils.isEmpty(videoFilePOPage.getRecords())) {
            return Collections.emptyList();
        }
        return videoFilePOPage.getRecords();
    }

    public List<VideoFilePO> getLocalStorageFailFiles(Integer maxSize, Integer maxRetryTimes, Date retryTime) {
        Page<VideoFilePO> videoFilePOPage = this.baseMapper.selectPage(new Page<>(1, maxSize), Wrappers.<VideoFilePO>lambdaQuery()
                                                                               .eq(VideoFilePO::getLocallyStored, Boolean.TRUE)
                                                                               .in(VideoFilePO::getLocallyStoredStatus,
                                                                                   Arrays.asList(VideoFileStatusEnum.LOCAL_FAILED_STORAGE, VideoFileStatusEnum.LOCAL_DELAY_STORAGE))
                                                                               .lt(VideoFilePO::getRetryTimes, maxRetryTimes)
                                                                               .lt(VideoFilePO::getRetryNextTime, retryTime)
                                                                      );
        if (CollectionUtils.isEmpty(videoFilePOPage.getRecords())) {
            return Collections.emptyList();
        }
        return videoFilePOPage.getRecords();
    }
}
