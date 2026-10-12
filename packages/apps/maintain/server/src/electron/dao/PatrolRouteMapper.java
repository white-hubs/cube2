package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRoutePageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolRouteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author : lijie
 * @date : 2023-5-25
 */
@Mapper
public interface PatrolRouteMapper extends BaseMapper<PatrolRoute> {
    List<PatrolLocationVO> queryById(@Param("uuid") Long uuid);

    Page<PatrolRouteVO> queryRoutePage(@Param("page") Page page, @Param("param") PatrolRoutePageParam param);

    List<PatrolRouteVO> queryRouteList(@Param("param") PatrolRoutePageParam param);

    List<PatrolRouteVO> queryRouteAll();

    List<PatrolLocationVO> queryLocationAllByRoute(@Param("uuid") Long uuid);

    List<PatrolRouteVO> queryByUuids(@Param("uuids") Set<Long> uuids);
}
