package com.guhecloud.platform.security.permission.interceptor;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import com.guhecloud.platform.security.permission.cache.PermissionCache;
import com.guhecloud.platform.security.permission.model.PrincipalDbDataFieldPermission;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataPermissionMethodInterceptor {

    private final PermissionCache permissionCache;

    @PostConstruct
    public void initialize() {
        log.info("Loading : dataPermissionMethodInterceptor");
    }

    @Before("@annotation(dataPermissionSwitch)")
    public void before(JoinPoint point, DataPermissionSwitch dataPermissionSwitch) {
        if (Boolean.TRUE.equals(SecurityContextHolder.isInnerServer())) {
            return;
        }
        if (dataPermissionSwitch == null) {
            log.trace("No annotations found");
            SecurityContextHolder.setDbDataPermissionSwitch(Boolean.FALSE);
            return;
        }
        if (StringUtils.isBlank(dataPermissionSwitch.dataModel())) {
            log.trace("No dataModel found");
            SecurityContextHolder.setDbDataPermissionSwitch(Boolean.FALSE);
            return;
        }
        if (Objects.isNull(SecurityContextHolder.getPrincipal(true))) {
            log.trace("No principal found");
            SecurityContextHolder.setDbDataPermissionSwitch(Boolean.FALSE);
            return;
        }
        SecurityContextHolder.setDbDataPermissionSwitch(dataPermissionSwitch.enable());
        //查询数据模型配置的数据权限范围集合
        Set<PrincipalDbDataFieldPermission> dbDataFieldPermissions = this.permissionCache
                .getPrincipalPermissions()
                .stream()
                .filter(principalMethodPermission ->
                                "DATA".equals(principalMethodPermission.getType()) &&
                                principalMethodPermission.getCode().equals(dataPermissionSwitch.dataModel()) &&
                                Objects.nonNull(principalMethodPermission.getDbDataFieldPermission()) &&
                                StringUtils.isNotBlank(principalMethodPermission.getDbDataFieldPermission().getScope()))
                .map(PrincipalMethodPermission::getDbDataFieldPermission)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(dbDataFieldPermissions)) {
            dbDataFieldPermissions = new HashSet<>();
        }
        //设置最小权限为当前用户
        PrincipalDbDataFieldPermission permission = new PrincipalDbDataFieldPermission();
        permission.setScope("CURRENT_USER");
        dbDataFieldPermissions.add(permission);
        // 如果有指定表
        if (StringUtils.isNotBlank(dataPermissionSwitch.tables())) {
            permission.setTables(StringUtils.split(dataPermissionSwitch.tables(), StringUtils.DEFAULT_DELIMITER_REGEX));
        }
        SecurityContextHolder.setDbDataPermission(JsonUtils.serialize(dbDataFieldPermissions));
    }
}
