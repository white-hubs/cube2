package com.guhecloud.cube.biz.ouaa.server.permission.common.request;

import lombok.Data;

import java.util.Set;

@Data
public class PermissionInterfaceModify {

    /**
     * 权限编号
     */
    private String code;

    /**
     * 接口编号集合
     */
    private Set<String> interfaceCodes;
}
