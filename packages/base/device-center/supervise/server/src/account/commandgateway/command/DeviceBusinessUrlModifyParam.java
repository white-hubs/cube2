package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 业务跳转地址配置
 */
@Data
public class DeviceBusinessUrlModifyParam {

    /**
     * 业务类型
     */
    @NotEmpty(message = "业务类型不能为空")
    @Size(max = 50, message = "业务类型不能大于50")
    private String businessType;

    /**
     * 业务类型名称
     */
    @Size(max = 50, message = "业务类型不能大于50")
    private String businessTypeName;

    /**
     * 访问端
     */
    @NotEmpty(message = "访问端不能为空")
    @Size(max = 50, message = "访问端不能大于50")
    private String terminal;

    /**
     * 跳转地址
     */
    @NotEmpty(message = "跳转地址不能为空")
    @Size(max = 50, message = "跳转地址不能大于100")
    private String url;
}
