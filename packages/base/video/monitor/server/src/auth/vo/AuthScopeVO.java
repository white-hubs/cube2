package com.guhecloud.cube.biz.vmonitor.server.auth.vo;

import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.ScopeTypeEnum;
import lombok.Data;

@Data
public class AuthScopeVO {

    /**
     * 范围类型
     */
    private ScopeTypeEnum scopeType;

    /**
     * 范围名称
     */
    private String scopeName;

    /**
     * 范围id
     */
    private Long scopeId;

    /**
     * 权限组id
     */
    private Long authGroupId;

}
