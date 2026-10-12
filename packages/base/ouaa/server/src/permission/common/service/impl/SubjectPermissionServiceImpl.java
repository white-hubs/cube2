package com.guhecloud.cube.biz.ouaa.server.permission.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.permission.common.dao.SubjectPermissionMapper;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.SubjectPermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.SubjectPermissionConfigModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.SubjectPermissionModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.SubjectPermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.SubjectPermissionVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.permission.model.Permission;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubjectPermissionServiceImpl extends ServiceImpl<SubjectPermissionMapper, SubjectPermissionPO> implements SubjectPermissionService {

    private final PermissionService permissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull SubjectPermissionModify subjectPermissionModify) {
        this.remove(subjectPermissionModify.getSubjectId(), subjectPermissionModify.getSubjectType(), true);
        if (CollectionUtils.isEmpty(subjectPermissionModify.getPermissionConfigs())) {
            return;
        }
        List<SubjectPermissionPO> subjectPermissionPOS = subjectPermissionModify.getPermissionConfigs().stream()
                .map(subjectPermissionConfig ->
                             SubjectPermissionPO.builder()
                                     .subjectId(subjectPermissionModify.getSubjectId())
                                     .subjectType(subjectPermissionModify.getSubjectType())
                                     .permissionCode(subjectPermissionConfig.getCode())
                                     .permissionExtentConfig(subjectPermissionConfig.getExtentConfig())
                                     .build()
                    ).collect(Collectors.toList());
        saveBatch(subjectPermissionPOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExtendConfig(@NonNull String subjectId, @NonNull SubjectTypeEnum subjectType,
                                 @NonNull Set<SubjectPermissionConfigModify> subjectPermissionConfigModifies
                                ) {
        for (SubjectPermissionConfigModify subjectPermissionConfigModify : subjectPermissionConfigModifies) {
            List<SubjectPermissionPO> subjectPermissionPOS = this.getSubjectPermissions(subjectId, subjectType, subjectPermissionConfigModify.getCode());
            if (CollectionUtils.isEmpty(subjectPermissionPOS)) {
                SubjectPermissionPO subjectPermissionPO = SubjectPermissionPO.builder()
                        .subjectId(subjectId)
                        .subjectType(subjectType)
                        .permissionCode(subjectPermissionConfigModify.getCode())
                        .permissionExtentConfig(subjectPermissionConfigModify.getExtentConfig())
                        .build();
                this.save(subjectPermissionPO);
            } else {
                subjectPermissionPOS.stream()
                        .forEach(subjectPermissionPO -> subjectPermissionPO.setPermissionExtentConfig(subjectPermissionConfigModify.getExtentConfig()));
                this.updateBatchById(subjectPermissionPOS);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String subjectId, @NonNull SubjectTypeEnum subjectType, Boolean isFunctionPermission) {
        if (isFunctionPermission) {
            List<SubjectPermissionPO> subjectPermissionPOS = this.list(Wrappers.<SubjectPermissionPO>lambdaQuery()
                                                                               .eq(SubjectPermissionPO::getSubjectId, subjectId)
                                                                               .eq(SubjectPermissionPO::getSubjectType, subjectType)
                                                                      );
            if (CollectionUtils.isEmpty(subjectPermissionPOS)) {
                return;
            }
            Set<String> permissionCodes = subjectPermissionPOS.stream()
                    .map(subjectPermissionPO -> subjectPermissionPO.getPermissionCode()).collect(Collectors.toSet());
            List<String> functionPermissionCodes = this.permissionService.list(Wrappers.<PermissionPO>lambdaQuery()
                                                                                       .eq(PermissionPO::getType, "FUNCATION")
                                                                                       .in(PermissionPO::getCode, permissionCodes)
                                                                              ).stream().map(PermissionPO::getCode)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(functionPermissionCodes)) {
                List<SubjectPermissionPO> subjectFunctionPermissions = subjectPermissionPOS.stream()
                        .filter(subjectPermission -> functionPermissionCodes.contains(subjectPermission.getPermissionCode()))
                        .collect(Collectors.toList());
                DatabaseContextHolder.softDelete(this, subjectFunctionPermissions);
            }
        } else {
            this.remove(subjectId, subjectType);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String subjectId, @NonNull SubjectTypeEnum subjectType) {
        this.remove(subjectId, subjectType, StringUtils.EMPTY_STRING);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String subjectId, @NonNull SubjectTypeEnum subjectType, String permissionCode) {
        List<SubjectPermissionPO> subjectPermissionPOS = this.getSubjectPermissions(subjectId, subjectType, permissionCode);
        if (!CollectionUtils.isEmpty(subjectPermissionPOS)) {
            DatabaseContextHolder.softDelete(this, subjectPermissionPOS);
        }
    }

    @Override
    public List<SubjectPermissionVO> getSubjectPermissions(Set<String> subjectIds, SubjectTypeEnum subjectType) {
        return this.baseMapper.selectAllPermissionsBySubjectIds(subjectIds, subjectType);
    }

    @Override
    public List<SubjectPermissionPO> getSubjectPermissions(String subjectId, SubjectTypeEnum subjectType) {
        return this.getSubjectPermissions(subjectId, subjectType, StringUtils.EMPTY_STRING);
    }

    @Override
    public Set<Permission> getPermissionsBySubjectIds(Set<String> subjectIds, SubjectTypeEnum subjectType) {
        return this.baseMapper.selectPermissionsBySubjectIds(subjectIds, subjectType);
    }

    @Override
    public List<SubjectPermissionPO> getSubjectPermissions(@NonNull String subjectId, @NonNull SubjectTypeEnum subjectType,
                                                           String permissionCode
                                                          ) {
        return this.list(Wrappers.<SubjectPermissionPO>lambdaQuery()
                                 .eq(SubjectPermissionPO::getSubjectId, subjectId)
                                 .eq(SubjectPermissionPO::getSubjectType, subjectType)
                                 .eq(StringUtils.isNotBlank(permissionCode), SubjectPermissionPO::getPermissionCode, permissionCode)
                        );
    }

    @Override
    public List<SubjectPermissionPO> getBindRolesPermission(@NonNull List<Long> roleIds, @NonNull List<String> codes) {
        if (ListUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(SubjectPermissionPO::getSubjectType, SubjectTypeEnum.ROLE)
                .in(SubjectPermissionPO::getSubjectId, roleIds)
                .in(SubjectPermissionPO::getPermissionCode, codes)
                .list();
    }
}
