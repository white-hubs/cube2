package com.guhecloud.cube.biz.vmonitor.server.auth.converters;

import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthScopePO;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthScopeVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class AuthScopeConvertForAuthScopeVO {

    private AuthScopeConvertForAuthScopeVO() {

    }

    public static AuthScopeVO toAuthScopeVO(AuthScopePO authScope) {
        AuthScopeVO authScopeVO = new AuthScopeVO();
        authScopeVO.setScopeId(authScope.getScopeId());
        authScopeVO.setScopeType(authScope.getScopeType());
        authScopeVO.setScopeName(authScope.getScopeName());
        authScopeVO.setAuthGroupId(authScope.getAuthGroupId());
        return authScopeVO;
    }

    public static List<AuthScopeVO> toAuthScopeVOS(List<AuthScopePO> authScopes) {
        if (CollectionUtils.isEmpty(authScopes)) {
            return CollectionUtils.emptyList();
        }
        return authScopes.stream().map(AuthScopeConvertForAuthScopeVO::toAuthScopeVO).toList();
    }
}
