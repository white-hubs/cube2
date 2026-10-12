package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RolePermissionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RolePermission;
import com.guhecloud.cube.biz.ouaa.server.service.RolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Override
    public Long save(Long roleId, Long permissionId) {
        RolePermission saved = this.baseMapper.selectOne(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId)
                                                                 .eq(RolePermission::getPermissionId, permissionId));
        if (Objects.isNull(saved)) {
            saved = RolePermission.builder().roleId(roleId).permissionId(permissionId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void remove(Long roleId, Long permissionId) {
        this.baseMapper.delete(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId)
                                       .eq(RolePermission::getPermissionId, permissionId));
    }

    @Override
    public void removeByRole(Long roleId) {
        this.baseMapper.delete(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId));
    }

    @Override
    public void removeByPermission(Long permissionId) {
        this.baseMapper.delete(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getPermissionId, permissionId));
    }

    @Override
    public List<RolePermission> listByRole(Long roleId) {
        return this.baseMapper.selectList(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId));
    }

    @Override
    public List<RolePermission> listByPermission(Long permissionId) {
        return this.baseMapper.selectList(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getPermissionId, permissionId));
    }

}
