package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RoleApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleApplication;
import com.guhecloud.cube.biz.ouaa.server.service.RoleApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleApplicationServiceImpl extends ServiceImpl<RoleApplicationMapper, RoleApplication> implements RoleApplicationService {

    @Override
    public Long save(Long roleId, Long terminalId, Long applicationId) {
        RoleApplication saved = this.baseMapper.selectOne(Wrappers.<RoleApplication>lambdaQuery().eq(RoleApplication::getRoleId, roleId)
                                                                  .eq(RoleApplication::getTerminalId, terminalId)
                                                                  .eq(RoleApplication::getApplicationId, applicationId));
        if (Objects.isNull(saved)) {
            saved = RoleApplication.builder().roleId(roleId).terminalId(terminalId).applicationId(applicationId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void remove(Long roleId, Long terminalId, Long applicationId) {
        this.baseMapper.delete(Wrappers.<RoleApplication>lambdaQuery().eq(RoleApplication::getRoleId, roleId)
                                       .eq(RoleApplication::getTerminalId, terminalId)
                                       .eq(RoleApplication::getApplicationId, applicationId));
    }

    @Override
    public void removeByRole(Long roleId) {
        this.baseMapper.delete(Wrappers.<RoleApplication>lambdaQuery().eq(RoleApplication::getRoleId, roleId));
    }

    @Override
    public void removeByTerminalApplication(Long terminalId, Long applicationId) {
        this.baseMapper.delete(Wrappers.<RoleApplication>lambdaQuery().eq(RoleApplication::getTerminalId, terminalId)
                                       .eq(RoleApplication::getApplicationId, applicationId));
    }

    @Override
    public List<RoleApplication> listByRoleTerminal(Long roleId, Long terminalId) {
        return this.baseMapper.selectList(Wrappers.<RoleApplication>lambdaQuery().eq(RoleApplication::getRoleId, roleId)
                                                  .eq(RoleApplication::getTerminalId, terminalId));
    }

    @Override
    public List<RoleApplication> listByTerminalApplication(Long terminalId, Long applicationId) {
        return this.baseMapper.selectList(Wrappers.<RoleApplication>lambdaQuery().eq(RoleApplication::getTerminalId, terminalId)
                                                  .eq(RoleApplication::getApplicationId, applicationId));
    }

    @Override
    public List<RoleApplication> listByTenant(Long tenant) {
        return this.baseMapper.listByTenant(tenant);
    }
}
