package com.guhecloud.cube.biz.vmonitor.server.auth.vo;

import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.GrantTargetEnum;
import lombok.Data;

import java.util.Set;

@Data
public class AuthGrantTargetVO {

    /**
     * 授权对象类型
     */
    private GrantTargetEnum targetType;

    /**
     * 授权对象id集合
     */
    private Set<Long> targetIds;

    /**
     * 权限组id
     */
    private Long authGroupId;
}
