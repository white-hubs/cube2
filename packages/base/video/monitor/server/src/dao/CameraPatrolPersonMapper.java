package com.guhecloud.cube.biz.vmonitor.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolPerson;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CameraPatrolPersonMapper extends BaseMapper<CameraPatrolPerson> {

    List<CameraPatrolPerson> selectCameraPatrolPersons(@Param("enabled") Boolean enabled, @Param("period") Period period);

}
