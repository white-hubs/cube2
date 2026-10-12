package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.RoamingRoutePointMapper;
import com.guhecloud.cube.base.visualization.server.entity.RoamingRoutePoint;
import com.guhecloud.cube.base.visualization.server.service.RoamingRoutePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:13
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoamingRoutePointServiceImpl extends ServiceImpl<RoamingRoutePointMapper, RoamingRoutePoint> implements RoamingRoutePointService {
}
