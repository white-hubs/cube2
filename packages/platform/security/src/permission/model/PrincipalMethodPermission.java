package com.guhecloud.platform.security.permission.model;

import lombok.Data;

import java.util.Set;

@Data
public class PrincipalMethodPermission extends PrincipalPermission {

    /**
     * 权限编号
     */
    private String code;

    /**
     * 权限类型
     */
    private String type;

    /**
     * 权限对应接口层方法(hasAuthority的表达式(业务域:资源:操作))
     */
    private Set<String> hasAuthorities;

    /**
     * 数据权限
     */
    private PrincipalDbDataFieldPermission dbDataFieldPermission;

}
