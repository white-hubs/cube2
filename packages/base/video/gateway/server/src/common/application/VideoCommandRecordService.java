package com.guhecloud.cube.base.videogw.server.common.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoCommandRecordPO;
import com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.mapper.VideoCommandRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VideoCommandRecordService extends ServiceImpl<VideoCommandRecordMapper,VideoCommandRecordPO> {
}
