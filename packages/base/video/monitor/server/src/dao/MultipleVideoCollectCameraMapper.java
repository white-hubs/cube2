package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollectCamera;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 多路视频摄像机mapper接口
 *
 * @author lanchun
 * @date 2021/12/6 19:54
 */
@Repository
public interface MultipleVideoCollectCameraMapper extends BaseMapper<MultipleVideoCollectCamera> {

    List<MultipleVideoCollectCamera> selectListByCollectId(@Param("userId") Long userId,
                                                           @Param("multipleVideoCollectId") Long multipleVideoCollectId
                                                          );

    List<MultipleVideoCollectCamera> selectListUserMultipleVideosBySource(@Param("userId") Long userId, @Param("source") String source);
}