package com.guhecloud.cube.base.videogw.server.common.infrastructure.repository;

import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoCommandRecordPO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.mapper.VideoCommandRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoCommandRecordRepository {

    private final VideoCommandRecordMapper videoCommandRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(VideoCommandRecordPO videoCommandRecord) {
        this.videoCommandRecordMapper.insert(videoCommandRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateResult(Long id, String status, String result) {
        VideoCommandRecordPO videoCommandRecordPO = this.videoCommandRecordMapper.selectById(id);
        if (Objects.nonNull(videoCommandRecordPO)) {
            videoCommandRecordPO.setResultParam(result);
            videoCommandRecordPO.setStatus(status);
            videoCommandRecordMapper.updateById(videoCommandRecordPO);
        }
    }
}
