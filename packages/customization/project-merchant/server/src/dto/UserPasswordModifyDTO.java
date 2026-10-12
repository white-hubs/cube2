package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UserPasswordModifyDTO {

    private Boolean fromPc = true;

    private String password;

    @NotNull(message = "新密码不能为空")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    private String againPassWord;

}
