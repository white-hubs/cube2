package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户组织机构关联数据模型
 *
 * @author Woody
 * @date 2021/7/1
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrganization implements Entity<Long> {
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
     * 岗位主键
     */
    private Long positionId;

    /**
     * 组织机构主键
     */
    private Long organizationId;
    /**
     * 职位主键
     */
    private Long appointmentId;
}
