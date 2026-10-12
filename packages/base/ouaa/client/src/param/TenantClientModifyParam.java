package com.guhecloud.cube.biz.ouaa.client.param;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 租户客户端保存配置
 *
 * @author lanchun
 * @date 2024/10/22 9:55
 */
@Data
public class TenantClientModifyParam {

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
    @Max(value = 999999999, message = "刷新令牌超时时间不能大于999999999秒")
    private Integer refreshTokenTimeout;
}
