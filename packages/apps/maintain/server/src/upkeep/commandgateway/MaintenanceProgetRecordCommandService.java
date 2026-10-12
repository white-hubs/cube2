package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceTasksCommand;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.po.MaintenanceProgetRecord;

import jakarta.validation.Valid;

import java.text.ParseException;

public interface MaintenanceProgetRecordCommandService extends IService<MaintenanceProgetRecord> {
    void saveProgetRecord(@Valid MaintenanceTasksCommand maintenanceTasksCommand) throws ParseException;
}
