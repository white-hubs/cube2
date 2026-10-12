package com.guhecloud.cube.biz.ouaa.server.permission.common.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class PermissionModify {

    /**
     * 权限编号
     */
    @NotBlank(message = "权限编号不能为空")
    @Size(max = 100, message = "权限编号不能大于100")
    private String code;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称不能大于50")
    private String name;

    /**
     * 权限描述
     */
    @Size(max = 200, message = "权限描述不能大于200")
    private String description;
}
