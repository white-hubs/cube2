package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 类型数据
 */
@Data
public class TypeDataParam {

    /**
     * 业务类型
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;
}
