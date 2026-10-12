package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenancePlanCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceUpdatePlanCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenancePlan;

public interface MaintenancePlanCommandService extends IService<MaintenancePlan> {
    void savePlan(MaintenancePlanCommand param);

    void updatePlan(MaintenanceUpdatePlanCommand param);

    void deletePlan(Long id);

}
