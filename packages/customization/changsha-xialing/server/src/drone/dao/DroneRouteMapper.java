package com.guhecloud.cube.customization.xialing.server.drone.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customization.xialing.server.drone.entity.DroneRoute;
import org.apache.ibatis.annotations.Mapper;

/**
 * 霞凝无人机航迹数据访问接口
 * <p>
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作。
 * 对应数据库表 drone_route。
 * </p>
 *
 * @author system
 */
@Mapper
public interface DroneRouteMapper extends BaseMapper<DroneRoute> {
}
