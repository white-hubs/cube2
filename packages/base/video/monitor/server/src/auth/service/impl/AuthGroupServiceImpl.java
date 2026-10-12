package com.guhecloud.cube.biz.vmonitor.server.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.vmonitor.server.auth.dao.AuthGroupMapper;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthGroupModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.query.AuthQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGroupPO;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthGrantTargetService;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthGroupService;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthScopeService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthGroupServiceImpl extends ServiceImpl<AuthGroupMapper, AuthGroupPO> implements AuthGroupService {

    private final AuthGroupMapper authGroupMapper;

    private final AuthScopeService authScopeService;

    private final AuthGrantTargetService authGrantTargetService;

    public AuthGroupServiceImpl(AuthGroupMapper authGroupMapper, AuthScopeService authScopeService,
                                AuthGrantTargetService authGrantTargetService
                               ) {
        this.authGroupMapper = authGroupMapper;
        this.authScopeService = authScopeService;
        this.authGrantTargetService = authGrantTargetService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long increaseAuthGroup(AuthGroupModifyParam param) {
        var authGroupPO = new AuthGroupPO();
        this.authGroupMapper.insert(authGroupPO);
        this.authScopeService.batchSaveAuthScopes(authGroupPO.getId(), param.getAuthScopes());
        this.authGrantTargetService.batchSaveAuthGrantTargets(authGroupPO.getId(), param.getGrantTargets());
        return authGroupPO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAuthGroup(Long id, AuthGroupModifyParam param) {
        authScopeService.removeByAuthGroupId(id);
        authGrantTargetService.removeByAuthGroupId(id);
        this.authScopeService.batchSaveAuthScopes(id, param.getAuthScopes());
        this.authGrantTargetService.batchSaveAuthGrantTargets(id, param.getGrantTargets());
    }

    @Override
    public AuthGroupPO getAuthGroup(Long id) {
        return this.authGroupMapper.selectById(id);
    }

    @Override
    public Page<AuthGroupPO> queryPage(AuthQueryParam param) {
        return this.authGroupMapper.selectPage(PagingUtils.convert(param), param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAuthGroup(Long id) {
        DatabaseContextHolder.softDelete(AuthGroupPO.class, id);
        authScopeService.removeByAuthGroupId(id);
        authGrantTargetService.removeByAuthGroupId(id);
    }
}
