package com.guhecloud.cube.base.videogw.server.common.infrastructure.repository;

import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.mapper.VideoFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoFileRepository {

    private final VideoFileMapper videoFileMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(VideoFilePO videoFilePO) {
        this.videoFileMapper.insert(videoFilePO);
    }
}
