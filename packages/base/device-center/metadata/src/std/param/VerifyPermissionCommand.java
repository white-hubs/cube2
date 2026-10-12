package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class VerifyPermissionCommand {
    @NotNull
    private Long deviceInstanceId;
    @NotBlank
    private String password;
}
