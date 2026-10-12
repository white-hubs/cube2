package com.guhecloud.cube.base.message2.server.sat.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class SatMsgSceneReceiveUserModifyParam {

    @NotNull(message = "用户主键不能为空")
    private Long userId;

    /**
     * 用户名称
     */
    @NotEmpty(message = "用户名称不能为空")
    @Size(message = "用户名称不能大于50")
    private String userName;
}
