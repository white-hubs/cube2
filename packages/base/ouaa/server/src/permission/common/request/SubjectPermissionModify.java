package com.guhecloud.cube.biz.ouaa.server.permission.common.request;

import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import lombok.Data;

import java.util.Set;

@Data
public class SubjectPermissionModify {

    /**
     * 主体主键
     */
    private String subjectId;

    /**
     * 主体类型
     */
    private SubjectTypeEnum subjectType;

    /**
     * 权限配置
     */
    private Set<SubjectPermissionConfigModify> permissionConfigs;

}
