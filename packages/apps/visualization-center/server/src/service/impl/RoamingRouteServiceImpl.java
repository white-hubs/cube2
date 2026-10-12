package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.RoamingRouteMapper;
import com.guhecloud.cube.base.visualization.server.entity.RoamingRoute;
import com.guhecloud.cube.base.visualization.server.entity.RoamingRoutePoint;
import com.guhecloud.cube.base.visualization.server.param.RoamingRouteParam;
import com.guhecloud.cube.base.visualization.server.param.RoamingRoutePointParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.RoamingRoutePointService;
import com.guhecloud.cube.base.visualization.server.service.RoamingRouteService;
import com.guhecloud.cube.base.visualization.server.vo.RoamingRoutePointVO;
import com.guhecloud.cube.base.visualization.server.vo.RoamingRouteVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:07
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoamingRouteServiceImpl extends ServiceImpl<RoamingRouteMapper, RoamingRoute> implements RoamingRouteService {
    private final RoamingRoutePointService roamingRoutePointService;

    @Override
    public List<RoamingRouteVO> queryRoamingRouteByProjectId(VisualizationBaseQueryParam param) {
        Assert.isParameterError(Objects.isNull(param.getProjectId()), "项目id不能为空");
        List<RoamingRoute> roamingRouteList = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getKeyword()), RoamingRoute::getName, param.getKeyword())
                .eq(RoamingRoute::getProjectId, param.getProjectId())
                .list();
        if (CollectionUtils.isEmpty(roamingRouteList)) {
            return new ArrayList<>();
        }
        List<Long> roamingRouteIds = roamingRouteList.stream().map(RoamingRoute::getId).collect(Collectors.toList());
        Map<Long, List<RoamingRoutePoint>> routePointMap = Optional
                .ofNullable(roamingRoutePointService.lambdaQuery()
                                    .in(RoamingRoutePoint::getRouteId, roamingRouteIds)
                                    .list())
                .orElse(new ArrayList<>())
                .stream().collect(Collectors.groupingBy(RoamingRoutePoint::getRouteId));
        return roamingRouteList.stream().map(t -> {
                                                 RoamingRouteVO roamingRouteVO = new RoamingRouteVO();
                                                 roamingRouteVO.setId(t.getId());
                                                 roamingRouteVO.setName(t.getName());
                                                 if (routePointMap.containsKey(t.getId())) {
                                                     List<RoamingRoutePointVO> roamingRoutePointVOS = routePointMap.get(t.getId())
                                                             .stream().map(point -> RoamingRoutePointVO.builder()
                                                                                   .id(point.getId())
                                                                                   .pointName(point.getPointName())
                                                                                   .routeId(point.getRouteId())
                                                                                   .routeName(t.getName())
                                                                                   .perspective(point.getPerspective())
                                                                                   .sort(point.getSort())
                                                                                   .speed(point.getSpeed())
                                                                                   .build()
                                                                          ).collect(Collectors.toList());
                                                     roamingRouteVO.setRoamingRoutePointList(roamingRoutePointVOS);
                                                 }
                                                 return roamingRouteVO;
                                             }
                                            ).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateRoamingRoute(RoamingRouteParam param) {
        if (Objects.nonNull(param.getId())) {
            RoamingRoute roamingRoute = this.getById(param.getId());
            Assert.isNotFound(Objects.isNull(roamingRoute), "漫游线路不存在");
            roamingRoute.setName(param.getName());
            roamingRoute.setProjectId(param.getProjectId());
            return this.updateById(roamingRoute);
        } else {
            RoamingRoute roamingRoute = new RoamingRoute();
            roamingRoute.setId(IdWorker.getId());
            roamingRoute.setName(param.getName());
            roamingRoute.setProjectId(param.getProjectId());
            return this.save(roamingRoute);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateRoamingPoint(RoamingRoutePointParam param) {
        Assert.isParameterError(Objects.isNull(param.getRouteId()), "漫游路线id不能为空");
        if (Objects.nonNull(param.getId())) {
            RoamingRoutePoint roamingRoutePoint = roamingRoutePointService.getById(param.getId());
            Assert.isNotFound(Objects.isNull(roamingRoutePoint), "漫游点位不存在");
            roamingRoutePoint.setPointName(param.getPointName());
            roamingRoutePoint.setRouteId(param.getRouteId());
            roamingRoutePoint.setPerspective(param.getPerspective());
            roamingRoutePoint.setSort(param.getSort());
            roamingRoutePoint.setSpeed(param.getSpeed());
            return roamingRoutePointService.updateById(roamingRoutePoint);
        } else {
            RoamingRoutePoint roamingRoutePoint = new RoamingRoutePoint();
            roamingRoutePoint.setId(IdWorker.getId());
            roamingRoutePoint.setPointName(param.getPointName());
            roamingRoutePoint.setRouteId(param.getRouteId());
            roamingRoutePoint.setPerspective(param.getPerspective());
            roamingRoutePoint.setSort(param.getSort());
            roamingRoutePoint.setSpeed(param.getSpeed());
            return roamingRoutePointService.save(roamingRoutePoint);
        }
    }
}
