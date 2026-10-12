package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 设备删除条件
 *
 * @author lanchun
 * @date 2024/8/20 9:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDeletedParam extends DeviceFilterPageParam {

    /**
     * 应用编号
     */
    @NotBlank(message = "应用编号不能为空")
    private String applicationCode;
}
