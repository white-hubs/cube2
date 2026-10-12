package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * po属性工具
 */
public class AttPoAttributeSet {
    private AttPoAttributeSet() {

    }

    public static <T extends BasicEnhancedTenantEntity<?>> void initAddBasePoAttribute(T t) {

        var date = LocalDateTime.now();
        t.setTenant(SessionContextHolder.getRequester().getTenant());
        t.setDeleted(false);
        // 获取当前登录用户
        var principal = SecurityContextHolder.getPrincipal(true);
        var loginUserId = principal == null ? "" : String.valueOf(principal.getId());
        var userType = principal == null ? ActorTypeEnum.SYSTEM : ActorTypeEnum.safeValueOf(principal.getType().name());
        EntityAuditor.populateAudit(t, loginUserId, loginUserId, userType, date);
    }

    public static <T extends BasicEnhancedTenantEntity<?>> void initUpdateBasePoAttribute(T t) {

        var date = LocalDateTime.now();
        // 获取当前登录用户
        var principal = SecurityContextHolder.getPrincipal(true);
        var loginUserId = principal == null ? "" : String.valueOf(principal.getId());
        var userType = principal == null ? ActorTypeEnum.SYSTEM : ActorTypeEnum.safeValueOf(principal.getType().name());
        EntityAuditor.populateUpdateAudit(t, loginUserId, loginUserId, userType, date);
    }
}
