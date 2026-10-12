package com.guhecloud.cube.biz.ouaa.server.entity;

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
@TableName("ouaa_role_menu_data_permission")
public class RoleMenuDataPermission implements Entity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 角色主键
     */
    private Long roleId;

    /**
     * 访问端ID
     */
    private Long terminalId;

    /**
     * 应用ID
     */
    private Long applicationId;

    /**
     * 菜单主键
     */
    private Long menuId;

    /**
     * 权限编码
     */
    private String permission;

    /**
     * 权限排序号
     */
    private int sortNo;

    /**
     * 自定义的业务键值，根据permission编码确定存的数据
     */
    private String businessKeys;
}
