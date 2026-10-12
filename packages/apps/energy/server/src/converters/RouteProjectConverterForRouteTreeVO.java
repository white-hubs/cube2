package com.guhecloud.cube.srv.gen.energymanagement.server.converters;

import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteTreeVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class RouteProjectConverterForRouteTreeVO {
    private RouteProjectConverterForRouteTreeVO() {

    }

    public static RouteTreeVO toRouteTreeVO(RouteProject routeProject) {
        var routeTreeVO = new RouteTreeVO();
        routeTreeVO.setId(routeProject.getId());
        routeTreeVO.setParentId(routeProject.getParentId());
        routeTreeVO.setLevel(routeProject.getLevel());
        routeTreeVO.setRouteName(routeProject.getRouteName());
        return routeTreeVO;
    }

    public static List<RouteTreeVO> toRouteTreeVOS(List<RouteProject> routeProjects) {
        if (CollectionUtils.isEmpty(routeProjects)) {
            return CollectionUtils.emptyList();
        }
        return routeProjects.stream().map(RouteProjectConverterForRouteTreeVO::toRouteTreeVO).toList();
    }
}
