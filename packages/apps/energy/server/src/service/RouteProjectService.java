package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.RouteProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.RouteProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteDeviceVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteProjectVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：区域分项配置服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface RouteProjectService extends IService<RouteProject> {

    Long add(RouteProjectParam param);

    void update(Long id, RouteProjectParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    RouteProjectVO getById(Long id);

    Pagination<RouteProjectVO> getPage(RouteProjectQueryParam param);

    List<RouteDeviceVo> getRouteDevices();

}