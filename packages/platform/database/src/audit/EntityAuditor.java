package com.guhecloud.platform.database.audit;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import com.guhecloud.platform.database.entity.enhanced.EnhancedCreatable;
import com.guhecloud.platform.database.entity.enhanced.EnhancedUpdatable;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public final class EntityAuditor {

    public static final String INNER_CREATE_BY = "inner";

    public static final String INNER_CREATOR_ID = "-1";

    public static void populateAudit(BasicEnhancedEntity<?> entity, String name, String id, ActorTypeEnum type,
                                     LocalDateTime dateTime
                                    ) {
        populateCreateAudit(entity, name, id, type, dateTime);
        populateUpdateAudit(entity, name, id, type, dateTime);
    }

    public static void populateAudit(BasicEnhancedEntity<?> entity) {
        var principalInfo = extractPrincipalInfo();
        if (Objects.nonNull(principalInfo)) {
            populateAudit(entity, principalInfo.name, principalInfo.id, principalInfo.type, DateUtils.now());
        } else {
            populateInnerAudit(entity);
        }
    }

    public static void populateAudit(PopulateAuditParam auditParam) {
        populateAudit(auditParam.entity, auditParam.name, auditParam.id, auditParam.type, DateUtils.now());
    }

    public static void populateUpdateAudit(populateUpdateAudit auditParam) {
        populateUpdateAudit(auditParam.entity, auditParam.name, auditParam.id, auditParam.type, DateUtils.now());
    }

    public static void populateUpdateAudit(EnhancedUpdatable<?> entity, String name, String id, ActorTypeEnum type,
                                           LocalDateTime dateTime
                                          ) {
        entity.setUpdateBy(name);
        entity.setUpdaterId(id);
        entity.setUpdaterType(Optional.ofNullable(type).orElse(ActorTypeEnum.SYSTEM));
        entity.setUpdateTime(dateTime);
    }

    public static void populateCreateAudit(EnhancedCreatable<?> entity, String name, String id, ActorTypeEnum type,
                                           LocalDateTime dateTime
                                          ) {
        entity.setCreateBy(name);
        entity.setCreatorId(id);
        entity.setCreatorType(Optional.ofNullable(type).orElse(ActorTypeEnum.SYSTEM));
        entity.setCreateTime(dateTime);
    }

    public static void populateCreateAudit(PopulateCreateAudit auditParam) {
        populateCreateAudit(auditParam.entity, auditParam.name, auditParam.id, auditParam.type, DateUtils.now());
    }

    public static void populateInnerAudit(BasicEnhancedEntity<?> entity) {
        populateInnerCreateAudit(entity);
        populateInnerUpdateAudit(entity);
    }

    public static void populateInnerCreateAudit(EnhancedCreatable<?> entity) {
        entity.setCreateBy(INNER_CREATE_BY);
        entity.setCreatorType(ActorTypeEnum.SYSTEM);
        entity.setCreateTime(DateUtils.now());
    }

    public static void populateInnerUpdateAudit(EnhancedUpdatable<?> entity) {
        entity.setUpdateBy(INNER_CREATE_BY);
        entity.setUpdaterType(ActorTypeEnum.SYSTEM);
        entity.setUpdateTime(DateUtils.now());
    }

    public static void populateCreateAudit(EnhancedCreatable<?> entity) {
        var principalInfo = extractPrincipalInfo();
        if (Objects.nonNull(principalInfo)) {
            populateCreateAudit(entity, principalInfo.name, principalInfo.id, principalInfo.type, DateUtils.now());
        } else {
            populateInnerCreateAudit(entity);
        }
    }

    public static void populateUpdateAudit(EnhancedUpdatable<?> entity) {
        var principalInfo = extractPrincipalInfo();
        if (Objects.nonNull(principalInfo)) {
            populateUpdateAudit(entity, principalInfo.name, principalInfo.id, principalInfo.type, DateUtils.now());
        } else {
            populateInnerUpdateAudit(entity);
        }
    }

    private static PrincipalInfo extractPrincipalInfo() {
        var principal = SecurityContextHolder.getPrincipal(true);
        if (principal == null) {
            return null;
        }
        return new PrincipalInfo(
                SecurityContextHolder.getPrincipalProperty(Principal::getAccount),
                Optional.ofNullable(SecurityContextHolder.getPrincipalProperty(Principal::getId))
                        .map(Object::toString)
                        .orElse(null),
                Optional.ofNullable(SecurityContextHolder.getPrincipalProperty(Principal::getType))
                        .map(type -> ActorTypeEnum.safeValueOf(type.name()))
                        .orElse(ActorTypeEnum.SYSTEM)
        );
    }

    public record PopulateAuditParam(BasicEnhancedEntity<?> entity, String name, String id, ActorTypeEnum type,
                                     LocalDateTime dateTime) {}

    public record PopulateCreateAudit(EnhancedCreatable<?> entity, String name, String id, ActorTypeEnum type,
                                      LocalDateTime dateTime) {}

    public record populateUpdateAudit(EnhancedUpdatable<?> entity, String name, String id, ActorTypeEnum type,
                                      LocalDateTime dateTime) {}

    private record PrincipalInfo(String name, String id, ActorTypeEnum type) {}
}
