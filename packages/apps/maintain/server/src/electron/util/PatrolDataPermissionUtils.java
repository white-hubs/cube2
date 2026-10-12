package com.guhecloud.cube.srv.gen.maintain.server.electron.util;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.PermissionFilterParamUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskLocationMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolTaskMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.model.PrincipalDbDataFieldPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/4 15:01
 * @Description: 电子巡更数据权限工具类
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolDataPermissionUtils {

    private final PatrolTaskMapper patrolTaskMapper;
    private final PatrolTaskLocationMapper patrolTaskLocationMapper;
    private final PermissionFilterParamUtils permissionFilterParamUtils;

    /**
     * 自定义设置数据权限
     */
    public LambdaQueryChainWrapper<PatrolTask> setDataPermission(LambdaQueryChainWrapper<PatrolTask> warpper, String type) {
        if (warpper == null) {
            warpper = ChainWrappers.lambdaQueryChain(patrolTaskMapper);
        }
        if (Objects.isNull(SecurityContextHolder.getPrincipal(true))) {
            return warpper;
        }
        buildWarpperParam(warpper, type, getFilterUserIdList());
        return warpper;
    }

    private List<Long> getFilterUserIdList() {
        List<Long> userIdList = new ArrayList<>();
        //判断用户是否时管理员 如果是管理员则不进行权限过滤
        if (SecurityContextHolder.isClient() ||
            SecurityContextHolder.isTenantAdmin()) {
            return userIdList;
        }
        //未配置数据权限
        if (Objects.isNull(SecurityContextHolder.getDbDataPermissionSwitch()) || !SecurityContextHolder.getDbDataPermissionSwitch()) {
            return userIdList;
        }
        String permissionData = SecurityContextHolder.getDbDataPermission();
        List<PrincipalDbDataFieldPermission> principalDbDataFieldPermissions = JsonUtils
                .deserialize(permissionData, new TypeReference<List<PrincipalDbDataFieldPermission>>() {
                });
        if (CollectionUtils.isEmpty(principalDbDataFieldPermissions)) {
            PrincipalDbDataFieldPermission permission = new PrincipalDbDataFieldPermission();
            permission.setScope("CURRENT_USER");
            principalDbDataFieldPermissions.add(permission);
        }
        if (principalDbDataFieldPermissions.stream().anyMatch(dataFieldPermission -> "ALL".equals(dataFieldPermission.getScope()))) {
            return userIdList;
        }
        //获取当前权限配置

        for (PrincipalDbDataFieldPermission permission : principalDbDataFieldPermissions) {
            if ("CURRENT_USER".equals(permission.getScope())) {
                userIdList.add(SecurityContextHolder.getPrincipal().getId());
            } else if ("CURRENT_USER_DEPT".equals(permission.getScope())) {
                List<Long> userDept = permissionFilterParamUtils.getPermissionFilterParamByUserDept();
                if (CollectionUtils.isEmpty(userDept)) {
                    continue;
                }
                userIdList.addAll(userDept);
            } else if ("CUSTOM_DEPT".equals(permission.getScope())) {
                List<Long> customDept = permissionFilterParamUtils.getPermissionFilterParamByCustomDept(permission.getFilterParams());
                if (CollectionUtils.isEmpty(customDept)) {
                    continue;
                }
                userIdList.addAll(customDept);
            } else {
                break;
            }
        }
        userIdList.removeIf(Objects::isNull);
        return userIdList;
    }

    /**
     * 构造查询参数
     *
     * @param warpper
     * @param type
     * @param userIdList
     */
    private void buildWarpperParam(LambdaQueryChainWrapper<PatrolTask> warpper, String type, List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<Long> finalUserIdList = userIdList.stream().distinct().collect(Collectors.toList());
        warpper.and(CollectionUtils.isNotEmpty(userIdList), t -> {
            for (Long userId : finalUserIdList) {
                t.or(q -> q.like(PatrolTask::getBelonger, userId)
                        .or()
                        .like(PatrolTask::getAssistIds, userId));
                /*switch (type) {
                    case "total":
                        t.or(q -> {
                            q.like(PatrolTask::getBelonger, userId).eq(PatrolTask::getStatus, PatrolTaskStateEnum.TO_BE_RECEIVED.name())
                                    .or().eq(PatrolTask::getUserId, userId)
                                    .or().like(PatrolTask::getAssistIds, userId);
                        });
                        break;
                    case "TO_BE_RECEIVED":
                        t.or(q -> {
                            q.like(PatrolTask::getBelonger, userId)
                                    .or()
                                    .like(PatrolTask::getAssistIds, userId);
                        });
                        break;
                    case "IN_EXECUTION":
                    case "FINISHED":
                        t.or(q -> {
                            q.eq(PatrolTask::getUserId, userId)
                                    .or()
                                    .like(PatrolTask::getAssistIds, userId);
                        });
                        break;
                    default:
                        break;

                }*/
            }
        });
    }

    public LambdaQueryChainWrapper<PatrolTaskLocation> setDataPermissionPatrolTaskLocation(
            LambdaQueryChainWrapper<PatrolTaskLocation> warpper
                                                                                          ) {
        if (warpper == null) {
            warpper = ChainWrappers.lambdaQueryChain(patrolTaskLocationMapper);
        }
        List<Long> userIdList = getFilterUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return warpper;
        }
        warpper.in(PatrolTaskLocation::getUserId, userIdList);
        return warpper;
    }
}
