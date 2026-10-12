package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.dao.TrafficLightMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.entity.TrafficLight;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.param.TrafficLightParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.service.TrafficLightService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.vo.TrafficLightVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficLightServiceImpl extends ServiceImpl<TrafficLightMapper, TrafficLight> implements TrafficLightService {

    private final TrafficLightMapper trafficLightMapper;

    @Override
    public Pagination<TrafficLightVo> getTrafficLightPage(TrafficLightParam param) {
        return PagingUtils.convert(trafficLightMapper.getTrafficLightPage(PagingUtils.convert(param), param));
    }
}
