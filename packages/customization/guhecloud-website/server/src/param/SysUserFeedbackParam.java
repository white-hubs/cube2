package com.guhecloud.cube.customized.guhecloudwebsite.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户留言新增参数
 */
@Data
public class SysUserFeedbackParam {

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String userName;

    /**
     * 联系方式
     */
    @NotBlank(message = "联系方式不能为空")
    private String contactWay;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 应用场景
     */
    private String applicationScenario;

}
