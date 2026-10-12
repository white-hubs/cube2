package com.guhecloud.cube.biz.ouaa.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserTenantDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 是否为管理员
     */
    private Boolean admin = false;

    /**
     * 是否最近登录
     */
    private Boolean recentLogin = false;

    /**
     * 是否为主企业
     */
    private Boolean mainEnterprise;
    /**
     * 工作状态
     */
    private String workStatus;
}
