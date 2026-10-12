package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RoleMenuDataPermissionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuDataPermission;
import com.guhecloud.cube.biz.ouaa.server.enumeration.DataPermissionEnum;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuDataPermissionService;
import com.guhecloud.cube.biz.ouaa.server.vo.DataPermissionVO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单功能服务实现
 *
 * @author lanchun
 * @date 2021/12/24 14:52
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleMenuDataPermissionServiceImpl extends ServiceImpl<RoleMenuDataPermissionMapper, RoleMenuDataPermission> implements RoleMenuDataPermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long roleId,
                     @NonNull Long terminalId,
                     @NonNull Long applicationId,
                     @NonNull Long menuId,
                     @NonNull String permission,
                     String businessKeys
                    ) {
        //菜单下的数据权限 只会有一条数据
        this.baseMapper.delete(Wrappers.<RoleMenuDataPermission>lambdaQuery()
                                       .eq(RoleMenuDataPermission::getRoleId, roleId)
                                       .eq(RoleMenuDataPermission::getTerminalId, terminalId)
                                       .eq(RoleMenuDataPermission::getApplicationId, applicationId)
                                       .eq(RoleMenuDataPermission::getMenuId, menuId));
        //保存当前的数据权限
        RoleMenuDataPermission roleMenuDataPermission = RoleMenuDataPermission.builder()
                .roleId(roleId)
                .terminalId(terminalId)
                .applicationId(applicationId)
                .menuId(menuId)
                .permission(permission)
                .sortNo(DataPermissionEnum.valueOf(permission).getSortNo())
                .businessKeys(businessKeys)
                .build();
        this.baseMapper.insert(roleMenuDataPermission);

        return roleMenuDataPermission.getId();
    }

    @Override
    public Long save(Long roleId, Long terminalId, Long applicationId, Long menuId, DataPermissionEnum dataPermission) {
        //保存当前的数据权限
        RoleMenuDataPermission roleMenuDataPermission = RoleMenuDataPermission.builder()
                .roleId(roleId)
                .terminalId(terminalId)
                .applicationId(applicationId)
                .menuId(menuId)
                .permission(dataPermission.name())
                .sortNo(dataPermission.getSortNo())
                .build();
        this.baseMapper.insert(roleMenuDataPermission);

        return roleMenuDataPermission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long roleId, @NonNull Long terminalId, @NonNull Long applicationId, @NonNull Long menuId,
                       @NonNull String permission
                      ) {
        RoleMenuDataPermission roleMenuDataPermission = this.baseMapper.selectOne(Wrappers.<RoleMenuDataPermission>lambdaQuery()
                                                                                          .eq(RoleMenuDataPermission::getRoleId, roleId)
                                                                                          .eq(RoleMenuDataPermission::getTerminalId, terminalId)
                                                                                          .eq(RoleMenuDataPermission::getApplicationId, applicationId)
                                                                                          .eq(RoleMenuDataPermission::getMenuId, menuId)
                                                                                          .eq(RoleMenuDataPermission::getPermission, permission));
        if (roleMenuDataPermission != null) {
            this.baseMapper.deleteById(roleMenuDataPermission.getId());
        }
    }

    @Override
    public List<DataPermissionVO> getRoleMenuDataPermission(List<Long> roleIds, Long terminalId, Long applicationId, Long menuId) {
        String roleIdsStr = roleIds.stream().map(id -> id.toString()).collect(Collectors.joining(","));
        return this.baseMapper.getRoleMenuDataPermission(roleIdsStr, terminalId, applicationId, menuId);
    }
    
    @Override
    public Collection<RoleMenuDataPermission> listByTenant(Long tenant) {
        return getBaseMapper().listByTenant(tenant);
    }

    @Override
    public void removeByRoleMenuIds(Collection<Long> recordIds) {
        getBaseMapper().removeByRoleMenuIds(recordIds);
    }

    @Override
    public List<RoleMenuDataPermission> getBindDataPermission(@NonNull List<Long> roleIds) {
        return lambdaQuery().in(RoleMenuDataPermission::getRoleId, roleIds).list();
    }
}
