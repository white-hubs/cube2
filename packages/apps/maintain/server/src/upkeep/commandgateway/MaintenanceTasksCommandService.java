package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceTasks;

public interface MaintenanceTasksCommandService extends IService<MaintenanceTasks> {
    void saveTask();

    void saveTask(String formatTime, MaintenancePlan plan);

    void updatePlan();
}
