package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.MaintenanceProgetRecordCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command.MaintenanceTasksCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.text.ParseException;

/**
 * @author lrj
 * @Description 维保结果记录
 * @date 2022-4-29 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceProgetRecordController {

    private final MaintenanceProgetRecordCommandService maintenanceProgetRecordCommandService;

    /**
     * @Description 记录新增
     * @Author lrj
     */
    @PostMapping("/save-proget-record")
    public void saveProgetRecord(@Valid @RequestBody MaintenanceTasksCommand maintenanceTasksCommand) throws ParseException {
        maintenanceProgetRecordCommandService.saveProgetRecord(maintenanceTasksCommand);
    }

}