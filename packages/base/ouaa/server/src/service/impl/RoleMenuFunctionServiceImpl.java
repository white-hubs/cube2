package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.RoleMenuFunctionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuFunction;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.RoleMenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.redis.authority.message.AuthorityMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色菜单功能服务实现
 *
 * @author lanchun
 * @date 2021/12/24 14:52
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleMenuFunctionServiceImpl extends ServiceImpl<RoleMenuFunctionMapper, RoleMenuFunction> implements RoleMenuFunctionService {

    private final FunctionService functionService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String REFRESH_AUTHORITY = "refreshAuthority";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long roleId, @NonNull Long terminalId, @NonNull Long applicationId, @NonNull Long menuId,
                     @NonNull Long functionId
                    ) {
        RoleMenuFunction roleMenuFunction = this.baseMapper.selectOne(Wrappers.<RoleMenuFunction>lambdaQuery()
                                                                              .eq(RoleMenuFunction::getRoleId, roleId)
                                                                              .eq(RoleMenuFunction::getTerminalId, terminalId)
                                                                              .eq(RoleMenuFunction::getApplicationId, applicationId)
                                                                              .eq(RoleMenuFunction::getMenuId, menuId)
                                                                              .eq(RoleMenuFunction::getFunctionId, functionId));
        if (Objects.isNull(roleMenuFunction)) {
            roleMenuFunction = RoleMenuFunction.builder()
                    .roleId(roleId)
                    .terminalId(terminalId)
                    .applicationId(applicationId)
                    .menuId(menuId)
                    .functionId(functionId)
                    .build();
            this.baseMapper.insert(roleMenuFunction);
        }
        this.publishRoleAuthority(Collections.singletonList(roleId));
        return roleMenuFunction.getId();
    }

    @Override
    public List<RoleMenuFunction> listRoleMenuFunctions(@NonNull Long roleId) {
        return this.lambdaQuery().eq(RoleMenuFunction::getRoleId, roleId).list();
    }

    @Override
    public List<RoleMenuFunction> listRoleMenuFunctions(@NonNull Long roleId, @NonNull Long menuId) {
        return this.lambdaQuery()
                .eq(RoleMenuFunction::getRoleId, roleId)
                .eq(RoleMenuFunction::getMenuId, menuId)
                .list();
    }

    @Override
    public List<RoleMenuFunction> listRoleMenuFunctions(@NonNull Collection<Long> roleIds) {
        return CollectionUtils.isEmpty(roleIds) ? Collections.emptyList()
                                                : this.lambdaQuery().in(RoleMenuFunction::getRoleId, roleIds).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long roleId, @NonNull Long terminalId, @NonNull Long applicationId, @NonNull Long menuId,
                       @NonNull Long functionId
                      ) {
        this.baseMapper.delete(Wrappers.<RoleMenuFunction>lambdaQuery()
                                       .eq(RoleMenuFunction::getRoleId, roleId)
                                       .eq(RoleMenuFunction::getMenuId, menuId)
                                       .eq(RoleMenuFunction::getFunctionId, functionId));
        this.publishRoleAuthority(Collections.singletonList(roleId));
    }

    @Override
    public void publishRoleAuthority(@NonNull Collection<Long> roleIds) {
        List<AuthorityMessage> authorityMessages = new ArrayList<>();
        //角色功能列表
        Map<Long, List<RoleMenuFunction>> roleFunctionMap = this.listRoleMenuFunctions(roleIds).stream()
                .collect(Collectors.groupingBy(roleMenuFunction -> roleMenuFunction.getRoleId()));
        for (Long roleId : roleIds) {
            AuthorityMessage authorityMessage = new AuthorityMessage();
            authorityMessage.setRoleId(roleId);
            //角色功能列表
            List<RoleMenuFunction> roleMenuFunctions = roleFunctionMap.get(roleId);
            if (!CollectionUtils.isEmpty(roleMenuFunctions)) {
                //功能列表
                Set<String> functionCodes = functionService
                        .listFunctions(roleMenuFunctions.stream().map(roleMenuFunction -> roleMenuFunction.getFunctionId())
                                               .collect(Collectors.toList()))
                        .stream().map(function -> function.getCode()).collect(Collectors.toSet());
                authorityMessage.setResources(functionCodes);
            } else {
                authorityMessage.setResources(Collections.EMPTY_SET);
            }
            authorityMessages.add(authorityMessage);
        }
        redisTemplate.convertAndSend(REFRESH_AUTHORITY, authorityMessages);
    }

    @Override
    public List<MenuFunctionVO> getRoleMenuFunctions(List<Long> roleIds, Long terminalId, Long applicationId, Long menuId) {
        String roleIdsStr = roleIds.stream().map(id -> id.toString()).collect(Collectors.joining(","));
        return this.baseMapper.getRoleMenuFunctions(roleIdsStr, terminalId, applicationId, menuId);
    }

    public List<RoleMenuFunction> listByTenant(Long tenantId) {
        return this.baseMapper.listByTenant(tenantId);
    }

    @Override
    public void removeByRoleMenuIds(Collection<Long> recordIds) {
        getBaseMapper().removeByRoleMenuIds(recordIds);
    }

    @Override
    public List<RoleMenuFunction> getBindFunctions(@NonNull List<Long> roleIds) {
        return lambdaQuery().in(RoleMenuFunction::getRoleId, roleIds).list();
    }
}
