package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 角色资源关联数据模型
 *
 * @author Woody
 * @date 2021/7/1
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission implements Entity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 权限主键
     */
    private Long permissionId;
}
