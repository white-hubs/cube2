package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceTasksVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceTasksQueryParam;

import java.util.List;

/**
 * 描述：维保任务服务层
 *
 * @author lrj
 * @date 2022-4-29 15:12:36
 */
public interface MaintenanceTasksService extends IService<MaintenanceTasks> {

    Page<MaintenanceTasksVo> selectTasks(MaintenanceTasksQueryParam param);

    Page<MaintenanceTasksVo> selectAppTasks(MaintenanceTasksQueryParam param);

    MaintenanceTasksVo selectTasksDetail(Long id);

    List<DeviceOperationStatisticsDTO> deviceMaintenanceNumStatistics(DeviceStatisticParam param);
}