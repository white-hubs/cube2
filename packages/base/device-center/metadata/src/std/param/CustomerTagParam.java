package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CustomerTagParam {
    private Long id;
    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    private String tagName;

    /**
     * 描述
     */
    private String description;
}
