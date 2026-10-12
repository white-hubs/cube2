package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceProjectCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProject;

import java.util.List;

public interface MaintenaceProjectCommandService extends IService<MaintenanceProject> {

    void save(List<MaintenanceProjectCommand> maintenanceProjectCommands);

}
