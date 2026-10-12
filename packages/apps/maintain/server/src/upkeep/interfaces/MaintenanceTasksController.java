package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo.MaintenanceTasksVo;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.MaintenanceTasksService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query.MaintenanceTasksQueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lrj
 * @Description 维保任务
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceTasksController {

    private final MaintenanceTasksService maintenanceTasksService;

    /**
     * @Description 维保任务列表
     * @Author lrj
     */
    @GetMapping("/select-tasks")
    public Page<MaintenanceTasksVo> selectTasks(MaintenanceTasksQueryParam param) {
        return maintenanceTasksService.selectTasks(param);
    }

    /**
     * @Description app维保任务列表
     * @Author lrj
     */
    @GetMapping("/select-app-tasks")
    public Page<MaintenanceTasksVo> selectAppTasks(MaintenanceTasksQueryParam param) {
        return maintenanceTasksService.selectAppTasks(param);
    }

    /**
     * @Description 维保任务详情
     * @Author lrj
     */
    @GetMapping("/select-tasks-detail/{id}")
    public MaintenanceTasksVo selectTasksDetail(@PathVariable String id) {
        return maintenanceTasksService.selectTasksDetail(Long.valueOf(id));
    }

    /**
     * 设备维保次数统计
     *
     * @param param 设备id
     *
     * @return 设备维保次数
     */
    @PostMapping("/upkeep/deviceMaintenanceNum")
    public List<DeviceOperationStatisticsDTO> deviceMaintenanceNumStatistics(@RequestBody DeviceStatisticParam param) {
        return maintenanceTasksService.deviceMaintenanceNumStatistics(param);
    }

}