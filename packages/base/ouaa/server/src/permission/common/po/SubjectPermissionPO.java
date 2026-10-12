package com.guhecloud.cube.biz.ouaa.server.permission.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
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
@TableName("subject_permission")
public class SubjectPermissionPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 主体主键
     */
    private String subjectId;

    /**
     * 主体类型
     */
    private SubjectTypeEnum subjectType;

    /**
     * 权限编号
     */
    private String permissionCode;

    /**
     * 权限配置扩充信息
     */
    private String permissionExtentConfig;
}
