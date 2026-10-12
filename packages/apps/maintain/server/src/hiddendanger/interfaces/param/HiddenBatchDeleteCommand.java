package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * 隐患单批量删除参数
 *
 * @author lanchun
 * @date 2025/4/23 13:45
 */
@Data
public class HiddenBatchDeleteCommand {

    @NotNull(message = "需要删除的隐患单不能为空")
    private Set<String> processInstanceIds;

}
