package com.guhecloud.cube.srv.gen.business.info.sync.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户组织关联表
 *
 * @TableName user_organization
 */
@Data
@TableName("user_organization")
public class UserOrganization implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 组织id
     */
    private Long organizationId;

    /**
     * 是否已删除
     */
    private Long deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    private Integer tenant;

    private static final long serialVersionUID = -5000678906425353041L;
}