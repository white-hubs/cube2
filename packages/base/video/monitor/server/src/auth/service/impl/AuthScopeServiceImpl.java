package com.guhecloud.cube.biz.vmonitor.server.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.vmonitor.server.auth.converters.AuthScopeConvertForAuthScopeVO;
import com.guhecloud.cube.biz.vmonitor.server.auth.dao.AuthScopeMapper;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthScopeModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthScopePO;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthScopeService;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthScopeVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthScopeServiceImpl implements AuthScopeService {

    private final AuthScopeMapper authScopeMapper;

    public AuthScopeServiceImpl(AuthScopeMapper authScopeMapper) {this.authScopeMapper = authScopeMapper;}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveAuthScopes(Long authGroupId, List<AuthScopeModifyParam> authScopeModifyParams) {
        authScopeModifyParams.forEach(scope -> {
            AuthScopePO authScopePO = new AuthScopePO();
            authScopePO.setAuthGroupId(authGroupId);
            authScopePO.setScopeType(scope.getScopeType());
            authScopePO.setScopeName(scope.getScopeName());
            authScopePO.setScopeId(scope.getScopeId());
            authScopeMapper.insert(authScopePO);
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateAuthScopeNameByScopeId(Long authScopeId, String authScopeName) {
        this.authScopeMapper.update(Wrappers.<AuthScopePO>lambdaUpdate()
                                            .set(AuthScopePO::getScopeName, authScopeName)
                                            .eq(AuthScopePO::getScopeId, authScopeId));
    }

    @Override
    public List<AuthScopePO> getAuthScopesByAuthGroupId(Long authGroupId) {
        return this.authScopeMapper.selectList(Wrappers.<AuthScopePO>lambdaQuery()
                                                       .eq(AuthScopePO::getAuthGroupId, authGroupId));
    }

    @Override
    public List<AuthScopeVO> getAuthScopesVOSByAuthGroupIds(Collection<Long> authGroupIds) {
        var authScopePOS = this.authScopeMapper.selectList(Wrappers.<AuthScopePO>lambdaQuery()
                                                                   .in(AuthScopePO::getAuthGroupId, authGroupIds));
        return AuthScopeConvertForAuthScopeVO.toAuthScopeVOS(authScopePOS);
    }

    @Override
    public List<AuthScopeVO> getAuthScopeVOSByAuthTargetIds(Collection<Long> authTargetIds) {
        var authScopePOS = this.authScopeMapper.selectAuthScopesByAuthTargetIds(authTargetIds);
        return AuthScopeConvertForAuthScopeVO.toAuthScopeVOS(authScopePOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByAuthGroupId(Long authGroupId) {
        var authScopePOS = getAuthScopesByAuthGroupId(authGroupId);
        if (!CollectionUtils.isEmpty(authScopePOS)) {
            DatabaseContextHolder.softDelete(AuthScopePO.class, authScopePOS.stream().map(AuthScopePO::getId).collect(Collectors.toSet()));
        }
    }
}
