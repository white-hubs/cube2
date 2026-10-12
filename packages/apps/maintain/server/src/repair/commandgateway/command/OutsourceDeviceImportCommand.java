package com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/5/19 14:59
 * @description
 */
@Data
public class OutsourceDeviceImportCommand {
    @NotEmpty
    private Set<Long> deviceInstanceIds;
}
