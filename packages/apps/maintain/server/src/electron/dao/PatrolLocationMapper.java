package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationAreaStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatrolLocationMapper extends BaseMapper<PatrolLocation> {

    List<PatrolLocationVO> queryLocationByPlan(@Param("planId") Long planId);

    List<LocationAreaStatisticsVO> locationDistributeStatistics(@Param("period") Period period);

    List<PatrolLocation> listByRoute(@Param("routeId") Long routeId);
}
