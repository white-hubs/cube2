package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.entity.TrafficLight;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.param.TrafficLightParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.vo.TrafficLightVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TrafficLightMapper extends BaseMapper<TrafficLight> {

    Page<TrafficLightVo> getTrafficLightPage(Page page, @Param("param") TrafficLightParam param);
}
