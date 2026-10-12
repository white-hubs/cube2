package com.guhecloud.cube.base.visualization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.visualization.server.entity.RoamingRoute;
import com.guhecloud.cube.base.visualization.server.param.RoamingRouteParam;
import com.guhecloud.cube.base.visualization.server.param.RoamingRoutePointParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.vo.RoamingRouteVO;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/2 11:07
 * @Description:
 */
public interface RoamingRouteService extends IService<RoamingRoute> {

    /**
     * 通过项目id查询漫游线路配置
     *
     * @param param 查询参数
     *
     * @return 实例对象
     */
    List<RoamingRouteVO> queryRoamingRouteByProjectId(VisualizationBaseQueryParam param);

    /**
     * 新增或更新漫游路线
     *
     * @param param 请求参数
     *
     * @return 是否成功
     */
    Boolean saveOrUpdateRoamingRoute(RoamingRouteParam param);

    /**
     * 新增或更新漫游点位
     *
     * @param param
     *
     * @return
     */
    Boolean saveOrUpdateRoamingPoint(RoamingRoutePointParam param);
}
