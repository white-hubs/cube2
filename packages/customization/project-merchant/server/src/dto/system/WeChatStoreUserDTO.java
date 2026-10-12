package com.guhecloud.cube.deliver.project.merchant.dto.system;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class WeChatStoreUserDTO {

    @NotNull
    @NotBlank
    private String projectId;

    private String userType;

    private String companyId;

    private String companyName;

    private String nickname;

    private Integer gender;

    private String mobile;

    private String wxopenId;
    private Integer tenant;
}
