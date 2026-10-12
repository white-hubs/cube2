package com.guhecloud.cube.biz.vmonitor.server.auth.param.commond;

import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.GrantTargetEnum;
import lombok.Data;

import java.util.Set;

@Data
public class AuthGrantTargetModifyParam {

    /**
     * 授权对象类型
     */
    private GrantTargetEnum targetType;

    /**
     * 授权对象id集合
     */
    private Set<Long> targetIds;
}
