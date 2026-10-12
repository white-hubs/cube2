package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UserRegisterDTO {

    @NotNull
    @NotBlank
    @Length(min = 4, max = 13)
    private String username;

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @NotBlank
    private String mobile;

    @NotNull
    @NotBlank
    @Length(min = 8, max = 18, message = "密码长度不合格，长度应在8-18位")
    private String password;

    @NotNull
    @NotBlank
    private String rePassword;

}
