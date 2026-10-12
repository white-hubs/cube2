package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.DeviceCategoryMaintenanceVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceProjectVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProject;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceProjectQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：维保配置项服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
public interface MaintenaceProjectService extends IService<MaintenanceProject> {

    Pagination<DeviceCategoryMaintenanceVO> selectMaintenaceProject(MaintenanceProjectQueryParam param);

    List<MaintenanceProjectVo> selectMaintenaceProjectDetail(MaintenanceProjectQueryParam param);
}