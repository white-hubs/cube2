package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 设备导入应用参数
 *
 * @author lanchun
 * @date 2024/4/30 9:56
 */
@Data
public class DeviceExportAppParam extends DeviceFilterPageParam {

    /**
     * 应用编号
     */
    @NotBlank(message = "应用编号不能为空")
    private String applicationCode;
}
