package com.guhecloud.platform.redis.authority.cache;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.platform.security.permission.cache.PermissionCache;
import com.guhecloud.platform.security.permission.model.Permission;
import com.guhecloud.platform.security.permission.model.PrincipalDbDataFieldPermission;
import com.guhecloud.platform.security.permission.model.PrincipalMethodPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrincipalPermissionCache implements PermissionCache {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_PERMISSION_KEY = "PERMISSION";

    @Override
    public void setPrincipalPermission(String principalId, Principal.Type principalType, Set<Permission> permissions) {
        String principalKey = buildPrincipalKey(principalId, principalType);
        Set<PrincipalMethodPermission> subjectMethodPermissions = new HashSet<>();
        permissions.forEach(permission -> {
            PrincipalMethodPermission subjectMethodPermission = new PrincipalMethodPermission();
            subjectMethodPermission.setPrincipalId(principalId);
            subjectMethodPermission.setPrincipalType(principalType);
            subjectMethodPermission.setCode(permission.getCode());
            subjectMethodPermission.setType(permission.getType());
            subjectMethodPermission.setHasAuthorities(permission.getInterfaceCodes());
            if (StringUtils.isNotBlank(permission.getExtentConfig())) {
                PrincipalDbDataFieldPermission subjectDbDataPermission = JsonUtils
                        .deserialize(permission.getExtentConfig(), PrincipalDbDataFieldPermission.class);
                subjectMethodPermission.setDbDataFieldPermission(subjectDbDataPermission);
            }
            subjectMethodPermissions.add(subjectMethodPermission);
        });
        redisTemplate.opsForHash().put(getPermissionRedisKey(), principalKey, subjectMethodPermissions);
    }

    @Override
    public Set<PrincipalMethodPermission> getPrincipalPermissions() {
        Object pop = redisTemplate.opsForHash().get(getPermissionRedisKey(), getPrincipalKey());
        return Objects.isNull(pop) ? Collections.emptySet() : (Set<PrincipalMethodPermission>) pop;
    }

    @Override
    public Set<PrincipalMethodPermission> getPrincipalPermissions(String permissionType) {
        var principalPermissions = getPrincipalPermissions();
        return CollectionUtils.isEmpty(principalPermissions) ? Collections.emptySet() : principalPermissions.stream()
                .filter(p -> p.getType().equals(permissionType)).collect(Collectors.toSet());
    }

    @Override
    public String getPrincipalKey() {
        return buildPrincipalKey(String.valueOf(SecurityContextHolder.getPrincipal().getId()), SecurityContextHolder.getPrincipal()
                .getType());
    }

    public String buildPrincipalKey(String principalId, Principal.Type principalType) {
        return principalType +
               ":" +
               principalId;
    }

    private String getPermissionRedisKey() {
        return REDIS_PERMISSION_KEY +
               ":" +
               SessionContextHolder.getRequester().getTenant();
    }
}
