package com.guhecloud.cube.biz.vmonitor.server.auth.service;

import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthScopeModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthScopePO;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthScopeVO;

import java.util.Collection;
import java.util.List;

public interface AuthScopeService {

    void batchSaveAuthScopes(Long authGroupId, List<AuthScopeModifyParam> authScopeModifyParams);

    void batchUpdateAuthScopeNameByScopeId(Long authScopeId, String authScopeName);

    List<AuthScopePO> getAuthScopesByAuthGroupId(Long authGroupId);

    List<AuthScopeVO> getAuthScopesVOSByAuthGroupIds(Collection<Long> authGroupIds);

    List<AuthScopeVO> getAuthScopeVOSByAuthTargetIds(Collection<Long> authTargetIds);

    void removeByAuthGroupId(Long authGroupId);
}
