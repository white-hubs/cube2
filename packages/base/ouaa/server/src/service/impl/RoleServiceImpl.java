package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RoleMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.param.RoleModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 角色业务操作实现
 *
 * @author Woody
 * @date 2021/6/30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public boolean deleteRole(@NonNull Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Role addRole(@NonNull RoleModifyParam param) {
        Role role = ObjectUtils.copying(param, Role.class);
        role.setSort(0F);
        role.setBuiltin(false);
        this.save(role);
        return role;
    }

    @Override
    public Role updateRole(@NonNull Long id, @NonNull RoleModifyParam param) {
        Role role = this.getById(id);
        Assert.isNotFound(role, "角色不存在");
        Assert.isNotAcceptable(role.getBuiltin(), "内置角色不可修改");
        ObjectUtils.copying(param, role);
        this.updateById(role);
        return role;
    }

    @Override
    public Page<Role> listRoles(@NonNull RoleQueryParam param) {
        return this.lambdaQuery()
                .like(!StringUtils.isEmpty(param.getName()), Role::getName, param.getName())
                .like(!StringUtils.isEmpty(param.getKeyword()), Role::getName, param.getKeyword())
                .eq(Objects.nonNull(param.getBuiltin()), Role::getBuiltin, param.getBuiltin())
                .eq(Objects.nonNull(param.getEnabled()), Role::getEnabled, param.getEnabled())
                .orderByAsc(Role::getSort).orderByDesc(Role::getId).page(PagingUtils.convert(param));
    }

    @Override
    public List<Role> listProductRoles() {
        return this.lambdaQuery().eq(Role::getEnabled, true)
                .orderByAsc(Role::getSort).orderByDesc(Role::getId).list();
    }

    @Override
    public Page<Role> pageProductRoles(@NonNull Paging paging) {
        return this.lambdaQuery().eq(Role::getEnabled, true)
                .orderByAsc(Role::getSort).orderByDesc(Role::getId).page(PagingUtils.convert(paging));
    }
}
