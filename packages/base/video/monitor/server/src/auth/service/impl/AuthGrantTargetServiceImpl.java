package com.guhecloud.cube.biz.vmonitor.server.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.vmonitor.server.auth.converters.AuthGrantTargetConvertForAuthGrantTargetVO;
import com.guhecloud.cube.biz.vmonitor.server.auth.dao.AuthGrantTargetMapper;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthGrantTargetModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGrantTargetPO;
import com.guhecloud.cube.biz.vmonitor.server.auth.service.AuthGrantTargetService;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthGrantTargetVO;
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
public class AuthGrantTargetServiceImpl implements AuthGrantTargetService {

    private final AuthGrantTargetMapper authGrantTargetMapper;

    public AuthGrantTargetServiceImpl(AuthGrantTargetMapper authGrantTargetMapper) {this.authGrantTargetMapper = authGrantTargetMapper;}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveAuthGrantTargets(Long authGroupId, List<AuthGrantTargetModifyParam> authGrantTargetModifyParams) {
        authGrantTargetModifyParams
                .forEach(target ->
                                 target.getTargetIds().forEach(targetId -> {
                                     AuthGrantTargetPO authGrantTargetPO = new AuthGrantTargetPO();
                                     authGrantTargetPO.setAuthGroupId(authGroupId);
                                     authGrantTargetPO.setTargetType(target.getTargetType());
                                     authGrantTargetPO.setTargetId(targetId);
                                     authGrantTargetMapper.insert(authGrantTargetPO);
                                 })
                        );
    }

    @Override
    public List<AuthGrantTargetPO> getAuthGrantTargetsByAuthGroupId(Long authGroupId) {
        return this.authGrantTargetMapper.selectList(Wrappers.<AuthGrantTargetPO>lambdaQuery()
                                                             .eq(AuthGrantTargetPO::getAuthGroupId, authGroupId));
    }

    @Override
    public List<AuthGrantTargetVO> getAuthGrantTargetVOSByAuthGroupIds(Collection<Long> authGroupIds) {
        var authGrantTargets = this.authGrantTargetMapper.selectList(Wrappers.<AuthGrantTargetPO>lambdaQuery()
                                                                             .in(AuthGrantTargetPO::getAuthGroupId, authGroupIds));
        return AuthGrantTargetConvertForAuthGrantTargetVO.toAuthGrantTypeTargetVOS(authGrantTargets);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByAuthGroupId(Long authGroupId) {
        var authGrantTargetPOS = getAuthGrantTargetsByAuthGroupId(authGroupId);
        if (!CollectionUtils.isEmpty(authGrantTargetPOS)) {
            DatabaseContextHolder.softDelete(AuthGrantTargetPO.class, authGrantTargetPOS.stream().map(AuthGrantTargetPO::getId)
                    .collect(Collectors.toSet()));
        }
    }
}
