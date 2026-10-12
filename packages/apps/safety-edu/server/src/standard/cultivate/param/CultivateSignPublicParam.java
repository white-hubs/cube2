package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 外部用户签到参数
 *
 * @author lanchun
 * @date 2024/4/11 11:50
 */
@Data
public class CultivateSignPublicParam {

    /**
     * 用户唯一标识
     */
    @Size(max = 50, message = "用户唯一标识不能大于50")
    @NotBlank(message = "用户唯一标识不能为空")
    private String userIdentify;

    /**
     * 用户名
     */
    @Size(max = 50, message = "用户名不能大于50")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 用户手机号
     */
    @Size(max = 20, message = "用户手机号不能大于50")
    @NotBlank(message = "用户手机号不能为空")
    private String userPhone;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String userDepartmentName;

    /**
     * 岗位名称
     */
    @Size(max = 50, message = "岗位名称不能大于50")
    private String userPostName;
}
