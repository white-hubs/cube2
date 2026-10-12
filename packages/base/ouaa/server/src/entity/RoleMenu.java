package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ouaa_role_menu")
public class RoleMenu implements Entity<Long> {

    private static final long serialVersionUID = 1L;

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
     * 访问端主键
     */
    private Long terminalId;

    /**
     * 应用主键
     */
    private Long applicationId;

    /**
     * 菜单主键
     */
    private Long menuId;

}
