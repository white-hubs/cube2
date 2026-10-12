package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 客户端分页对象
 *
 * @author lanchun
 * @date 2021/10/25 14:05
 */
@Data
public class ClientPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 客户端编号
     */
    private String code;

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 客户端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 租户编号
     */
    private String tenant;

    /**
     * 租户编号集合
     */
    private List<String> tenants;

    /**
     * 资源范围（多个范围之间使用","号隔开）
     */
    private String scopes;

    /**
     * 授权类型（多个类型之间使用","号隔开）
     */
    private String grantTypes;

    /**
     * 访问令牌超时时间（秒）
     */
    private Integer accessTokenTimeout;

    /**
     * 刷新令牌超时时间（秒）
     */
    private Integer refreshTokenTimeout;
}
