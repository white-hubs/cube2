package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RoleMenuMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public Long save(Long roleId, Long terminalId, Long applicationId, Long menuId) {
        RoleMenu saved = this.baseMapper.selectOne(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId)
                                                           .eq(RoleMenu::getTerminalId, terminalId)
                                                           .eq(RoleMenu::getApplicationId, applicationId).eq(RoleMenu::getMenuId, menuId));
        if (Objects.isNull(saved)) {
            saved = RoleMenu.builder().roleId(roleId).terminalId(terminalId).applicationId(applicationId).menuId(menuId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void remove(Long roleId, Long terminalId, Long applicationId, Long menuId) {
        this.baseMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId).eq(RoleMenu::getTerminalId, terminalId)
                                       .eq(RoleMenu::getApplicationId, applicationId).eq(RoleMenu::getMenuId, menuId));
    }

    @Override
    public void removeByRole(Long roleId) {
        this.baseMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
    }

    @Override
    public void removeByTerminalApplicationMenu(Long terminalId, Long applicationId, Long menuId) {
        this.baseMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getTerminalId, terminalId)
                                       .eq(RoleMenu::getApplicationId, applicationId).eq(RoleMenu::getMenuId, menuId));
    }

    @Override
    public List<RoleMenu> listByRoleTerminalApplication(Long roleId, Long terminalId, Long applicationId) {
        return this.baseMapper.listByRoleTerminalApplication(roleId, terminalId, applicationId);
    }

    @Override
    public List<RoleMenu> listByRoleTerminalApplication(Long roleId, Long terminalId) {
        return this.baseMapper.listByRoleTerminal(roleId, terminalId);
    }

    @Override
    public List<RoleMenu> listByTerminalApplicationMenu(Long terminalId, Long applicationId, Long menuId) {
        return this.baseMapper.selectList(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getTerminalId, terminalId)
                                                  .eq(RoleMenu::getApplicationId, applicationId).eq(RoleMenu::getMenuId, menuId));
    }

    @Override
    public List<RoleMenu> listByTenant(Long tenant) {
        return getBaseMapper().listByTenant(tenant);
    }

    @Override
    public List<RoleMenu> listByRoleTerminalApplicationBatch(Collection<Long> roleIds, Collection<Long> terminalIdList,
                                                             Collection<Long> applicationIdList
                                                            ) {
        if (CollectionUtils.isEmpty(roleIds) || CollectionUtils.isEmpty(terminalIdList) || CollectionUtils.isEmpty(applicationIdList)) {
            return new ArrayList<>();
        }
        return this.baseMapper.listByRoleTerminalApplicationBatch(roleIds, terminalIdList, applicationIdList);
    }

    @Override
    public List<RoleMenu> getBindMenus(@NonNull List<Long> roleIds) {
        return lambdaQuery().in(RoleMenu::getRoleId, roleIds).list();
    }
}
