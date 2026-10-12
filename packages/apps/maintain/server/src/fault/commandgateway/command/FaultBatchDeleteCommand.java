package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * 故障单批量删除参数
 *
 * @author lanchun
 * @date 2025/4/23 13:45
 */
@Data
public class FaultBatchDeleteCommand {

    @NotNull(message = "需要删除的报修单不能为空")
    private Set<String> processInstanceIds;

}
