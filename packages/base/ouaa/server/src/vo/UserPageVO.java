package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/10/9
 */
@Data
public class UserPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 角色
     */
    private String userRoleName;

    //部门
    private String userOrganizationName;

}
