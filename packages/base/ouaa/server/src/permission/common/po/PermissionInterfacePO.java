package com.guhecloud.cube.biz.ouaa.server.permission.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("permission_interface")
public class PermissionInterfacePO extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 权限编号
     */
    private String permissionCode;

    /**
     * 接口权限编号
     */
    private String interfaceCode;
}
