package com.guhecloud.cube.biz.vmonitor.server.auth.param.commond;

import lombok.Data;

import java.util.List;

@Data
public class AuthGroupModifyParam {

    /**
     * 授权范围集合
     */
    private List<AuthScopeModifyParam> authScopes;

    /**
     * 授权对象集合
     */
    private List<AuthGrantTargetModifyParam> grantTargets;
}
