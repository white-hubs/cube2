package com.guhecloud.cube.biz.mdata.server.scene.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.SceneType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场景类型mapper接口
 *
 * @author lanchun
 * @date 2021/12/2 17:40
 */
@Mapper
public interface SceneTypeMapper extends BaseMapper<SceneType> {

    List<SceneType> getBySequences(@Param("sequences") List<String> sequences, @Param("projectId") Long projectId);
}
