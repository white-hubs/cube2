package com.guhecloud.cube.biz.meattrade.server.base.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 屠宰场信息
 */
@Data
public class AbattoirVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 屠宰场编号
     */
    private String code;

    /**
     * 屠宰场名称
     */
    private String name;

    /**
     * 屠宰场简称
     */
    private String abbr;

    /**
     * 屠宰场地址
     */
    private String address;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 租户编号
     */
    private Long tenant;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新用户
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
