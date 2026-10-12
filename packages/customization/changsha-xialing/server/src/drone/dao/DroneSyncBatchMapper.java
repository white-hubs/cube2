package com.guhecloud.cube.customization.xialing.server.drone.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneSyncBatch;
import org.apache.ibatis.annotations.Mapper;

/**
 * 霞凝无人机同步批次数据访问接口
 * <p>
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作。
 * 对应数据库表 drone_sync_batch。
 * </p>
 *
 * @author system
 */
@Mapper
public interface DroneSyncBatchMapper extends BaseMapper<DroneSyncBatch> {
}
