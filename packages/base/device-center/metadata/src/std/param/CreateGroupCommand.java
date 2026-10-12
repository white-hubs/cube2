package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 创建分组
 *
 * @author lanchun
 * @date 2025/4/11 17:08
 */
@Data
public class CreateGroupCommand {

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    @Size(max = 50, message = "分组名称不能大于300")
    private String name;

    /**
     * 描叙信息
     */
    @Size(max = 300, message = "描述信息不能大于300")
    private String remark;
}
