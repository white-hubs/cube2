package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 客户端保存参数
 *
 * @author lanchun
 * @date 2021/10/25 13:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientModifyParam {

    /**
     * 客户端编号
     */
    @NotEmpty(message = "客户端编号不能为空")
    @Size(max = 100, message = "客户端编号不能大于100个字符")
    private String code;

    /**
     * 客户端名称
     */
    @NotEmpty(message = "客户端名称不能为空")
    @Size(max = 100, message = "客户端名称不能大于100个字符")
    private String name;

    /**
     * 客户端备注
     */
    @Size(max = 100, message = "客户端备注不能大于200个字符")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 租户
     */
    @NotNull(message = "租户不能为空")
    private List<String> tenants;

    /**
     * 访问端密码
     */
    @NotEmpty(message = "访问端密码不能为空")
    @Size(max = 20, message = "访问端密码不能大于20个字符")
    private String password;

    /**
     * 资源范围（多个范围之间使用","号隔开）
     */
    private String scopes;

    /**
     * 授权类型（多个类型之间使用","号隔开）
     */
    @NotEmpty(message = "授权类型不能为空")
    @Size(max = 100, message = "授权类型不能超过100个字符")
    private String grantTypes;

    /**
     * 访问令牌超时时间（秒）
     */
    @NotNull(message = "访问令牌超时时间不能为空")
    @Max(value = 999999999, message = "问令牌超时时间不能大于999999999秒")
    private Integer accessTokenTimeout;

    /**
     * 刷新令牌超时时间（秒）
     */
    @NotNull(message = "刷新令牌超时时间不能为空")
    @Max(value = 999999999, message = "刷新令牌超时时间不能大于999999999秒")
    private Integer refreshTokenTimeout;
}
