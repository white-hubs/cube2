package com.guhecloud.cube.base.visualization.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.visualization.server.entity.ModelRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  14:04
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface ModelRouteMapper extends BaseMapper<ModelRoute> {
    Integer maxLevel(@Param("moderlId") Long moderlId);
}
