package com.guhecloud.cube.base.videogw.server.common.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.videogw.server.common.domain.po.VideoFilePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoFileMapper extends BaseMapper<VideoFilePO> {
}
