package com.guhecloud.cube.base.videogw.server.common.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.mapper.VideoFileMapper;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoFileVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoFileQueryService {

    private final VideoFileMapper videoFileMapper;

    public VideoFileVO getById(@NonNull Long id) {
        VideoFilePO videoFilePO = this.videoFileMapper.selectById(id);
        return ObjectUtils.copying(videoFilePO, VideoFileVO.class);
    }

    public List<VideoFileVO> getByIds(Set<Long> ids) {
        List<VideoFilePO> videoFilePOS = this.videoFileMapper.selectList(Wrappers.<VideoFilePO>lambdaQuery()
                                                                                 .in(VideoFilePO::getId, ids)
                                                                        );
        if (CollectionUtils.isEmpty(videoFilePOS)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(videoFilePOS, VideoFileVO.class);
    }
}
