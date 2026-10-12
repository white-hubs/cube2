package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.entity.RoamingRoute;
import com.guhecloud.cube.base.visualization.server.param.RoamingRouteParam;
import com.guhecloud.cube.base.visualization.server.param.RoamingRoutePointParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.RoamingRouteService;
import com.guhecloud.cube.base.visualization.server.vo.RoamingRouteVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 漫游线路接口
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:08
 * @Description:
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/roaming/route")
public class RoamingRouteController {
    private final RoamingRouteService roamingRouteService;

    /**
     * 通过项目id查询漫游线路配置
     *
     * @param param 查询参数
     *
     * @return 实例对象
     */
    @GetMapping("/get")
    public List<RoamingRouteVO> queryRoamingRouteByProjectId(VisualizationBaseQueryParam param) {
        return roamingRouteService.queryRoamingRouteByProjectId(param);
    }

    /**
     * 新增或更新漫游路线
     */
    @PostMapping("/save/or/update")
    public Boolean saveOrUpdateRoamingRoute(@RequestBody RoamingRouteParam param) {
        return roamingRouteService.saveOrUpdateRoamingRoute(param);
    }

    /**
     * 删除漫游路线
     */
    @DeleteMapping("/{id}")
    public Boolean deleteRoamingRouteById(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(RoamingRoute.class, id);
    }

    /**
     * 新增或更新漫游点位
     */
    @PostMapping("/point/save/or/update")
    public Boolean saveOrUpdateRoamingPoint(@RequestBody RoamingRoutePointParam param) {
        return roamingRouteService.saveOrUpdateRoamingPoint(param);
    }

    /**
     * 删除漫游点位
     */
    @DeleteMapping("/point/{id}")
    public Boolean deleteRoamingRoutePointById(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(RoamingRoute.class, id);
    }

}
