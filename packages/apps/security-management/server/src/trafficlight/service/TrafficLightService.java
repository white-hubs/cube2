package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.entity.TrafficLight;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.param.TrafficLightParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.vo.TrafficLightVo;
import com.guhecloud.platform.base.model.Pagination;

public interface TrafficLightService extends IService<TrafficLight> {
    Pagination<TrafficLightVo> getTrafficLightPage(TrafficLightParam param);
}
