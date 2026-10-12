package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LineLossEditParam {
    @NotNull(message = "线路不能为空")
    private Long id;
    /**
     * 输入设备id
     */
    @NotNull(message = "输入设备不能为空")
    private Long fromDeviceId;
    /**
     * 输出设备id
     */
    @NotNull(message = "输出设备不能为空")
    private Long toDeviceId;
}
