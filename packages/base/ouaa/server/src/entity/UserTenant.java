package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户租户关联数据模型
 *
 * @author Woody
 * @date 2021/7/13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserTenant implements Entity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 归属关系
     */
    private String belongTenant;
}
