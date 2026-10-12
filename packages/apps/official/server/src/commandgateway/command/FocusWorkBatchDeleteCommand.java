package com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Data
public class FocusWorkBatchDeleteCommand {

    /**
     * 流程实例id集合
     */
    @NotNull(message = "删除的督办事项不能为空")
    private Set<String> processInstanceIds;
}
