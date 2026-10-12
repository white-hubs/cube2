package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RoleTerminalMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleTerminal;
import com.guhecloud.cube.biz.ouaa.server.service.RoleTerminalService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleTerminalServiceImpl extends ServiceImpl<RoleTerminalMapper, RoleTerminal> implements RoleTerminalService {

    @Override
    public Long save(Long roleId, Long terminalId) {
        RoleTerminal saved = this.baseMapper.selectOne(Wrappers.<RoleTerminal>lambdaQuery().eq(RoleTerminal::getRoleId, roleId)
                                                               .eq(RoleTerminal::getTerminalId, terminalId));
        if (Objects.isNull(saved)) {
            saved = RoleTerminal.builder().roleId(roleId).terminalId(terminalId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void remove(Long roleId, Long terminalId) {
        this.baseMapper.delete(Wrappers.<RoleTerminal>lambdaQuery().eq(RoleTerminal::getRoleId, roleId)
                                       .eq(RoleTerminal::getTerminalId, terminalId));
    }

    @Override
    public void removeByRole(Long roleId) {
        this.baseMapper.delete(Wrappers.<RoleTerminal>lambdaQuery().eq(RoleTerminal::getRoleId, roleId));
    }

    @Override
    public void removeByTerminal(Long terminalId) {
        this.baseMapper.delete(Wrappers.<RoleTerminal>lambdaQuery().eq(RoleTerminal::getTerminalId, terminalId));
    }

    @Override
    public List<RoleTerminal> listByRole(Long roleId) {
        return this.baseMapper.selectList(Wrappers.<RoleTerminal>lambdaQuery().eq(RoleTerminal::getRoleId, roleId));
    }

    @Override
    public List<RoleTerminal> listByRoleIds(@NonNull Collection<Long> roleIds) {
        return CollectionUtils.isEmpty(roleIds) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<RoleTerminal>lambdaQuery()
                                                  .in(RoleTerminal::getRoleId, roleIds));
    }

    @Override
    public List<RoleTerminal> listByTerminal(Long terminalId) {
        return this.baseMapper.selectList(Wrappers.<RoleTerminal>lambdaQuery().eq(RoleTerminal::getTerminalId, terminalId));
    }

    public List<RoleTerminal> listByTenant(Long tenant) {
        return this.baseMapper.listByTenant(tenant);
    }
}
