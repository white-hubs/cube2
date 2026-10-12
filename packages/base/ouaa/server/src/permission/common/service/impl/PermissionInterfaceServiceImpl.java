package com.guhecloud.cube.biz.ouaa.server.permission.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.permission.common.dao.PermissionInterfaceMapper;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionInterfacePO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.PermissionInterfaceModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionInterfaceService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionInterfaceServiceImpl extends ServiceImpl<PermissionInterfaceMapper, PermissionInterfacePO> implements PermissionInterfaceService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(PermissionInterfaceModify permissionInterfaceModify) {
        this.removeByPermissionCode(permissionInterfaceModify.getCode());
        if (!CollectionUtils.isEmpty(permissionInterfaceModify.getInterfaceCodes())) {
            List<PermissionInterfacePO> permissionInterfacePOS = permissionInterfaceModify.getInterfaceCodes().stream().map(interfaceCode ->
                                                                                                                                    PermissionInterfacePO
                                                                                                                                            .builder()
                                                                                                                                            .permissionCode(permissionInterfaceModify.getCode())
                                                                                                                                            .interfaceCode(interfaceCode)
                                                                                                                                            .build()
                                                                                                                           )
                    .collect(Collectors.toList());
            this.saveBatch(permissionInterfacePOS);
        }
    }

    @Override
    public List<PermissionInterfacePO> getPermissionInterfacesByCode(@NonNull String code) {
        return this.getPermissionInterfacesByCodes(Collections.singleton(code));
    }

    @Override
    public List<PermissionInterfacePO> getPermissionInterfacesByCodes(Set<String> codes) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList() :
               this.baseMapper.selectList(Wrappers.<PermissionInterfacePO>lambdaQuery()
                                                  .in(PermissionInterfacePO::getPermissionCode, codes));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByPermissionCode(@NonNull String code) {
        List<PermissionInterfacePO> permissionInterfaces = this.getPermissionInterfacesByCode(code);
        if (!CollectionUtils.isEmpty(permissionInterfaces)) {
            DatabaseContextHolder.softDelete(this, permissionInterfaces);
        }
    }
}
