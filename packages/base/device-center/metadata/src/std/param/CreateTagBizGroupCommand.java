package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author lanchun
 * @date 2025/4/14 10:39
 */
@Data
public class CreateTagBizGroupCommand {

    /**
     * 标签编码
     */
    @NotBlank(message = "标签编码不能为空")
    @Size(max = 200, message = "标签编码不能大于200")
    private String code;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 200, message = "标签名称不能大于200")
    private String name;

    /**
     * 设备分组
     */
    @NotNull(message = "设备分组信息不能为空")
    private CreateGroupCommand deviceGroup;
}
