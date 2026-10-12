package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.offline.model.po.InspectOfflineSyncChunkPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 离线同步分片表访问接口。
 */
@Mapper
public interface InspectOfflineSyncChunkMapper extends BaseMapper<InspectOfflineSyncChunkPO> {
}
