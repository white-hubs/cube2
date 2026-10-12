package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.util;

import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.util.function.Supplier;

public final class RiskDataPermissionContext {
    private RiskDataPermissionContext() {
    }

    public static <T> T withoutDataPermission(Supplier<T> supplier) {
        Boolean previous = SecurityContextHolder.getDbDataPermissionSwitch();
        try {
            SecurityContextHolder.setDbDataPermissionSwitch(Boolean.FALSE);
            return supplier.get();
        } finally {
            SecurityContextHolder.setDbDataPermissionSwitch(Boolean.TRUE.equals(previous));
        }
    }

    public static void withoutDataPermission(Runnable runnable) {
        withoutDataPermission(() -> {
            runnable.run();
            return null;
        });
    }
}
