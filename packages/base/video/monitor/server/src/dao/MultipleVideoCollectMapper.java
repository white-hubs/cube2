package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollect;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 多路视频mapper接口
 *
 * @author lanchun
 * @date 2021/12/6 19:54
 */
@Repository
public interface MultipleVideoCollectMapper extends BaseMapper<MultipleVideoCollect> {

    Page<MultipleVideoCollect> getPage(@Param("page") Page<MultipleVideoCollect> page, @Param("param") MultipleVideoCollectQueryParam param,
                                       @Param("userId") Long userId
                                      );

}