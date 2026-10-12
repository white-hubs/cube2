package com.guhecloud.platform.base.util;

import java.util.Objects;

/**
 * 脱敏字段编辑回传判定工具。
 */
public final class MaskedValueUtils {
    private MaskedValueUtils() {}

    public enum UpdateDecision {
        UNCHANGED,
        UPDATE,
        CLEAR
    }

    public static boolean containsMask(String value) {
        return value != null && value.indexOf(DesensitizeUtils.SYMBOL) >= 0;
    }

    public static UpdateDecision decide(String currentMasked, String incoming, boolean blankMeansClear) {
        if (incoming == null) {
            return UpdateDecision.UNCHANGED;
        }
        String value = incoming.trim();
        if (StringUtils.isEmpty(value)) {
            return blankMeansClear ? UpdateDecision.CLEAR : UpdateDecision.UPDATE;
        }
        if (!containsMask(value)) {
            return UpdateDecision.UPDATE;
        }
        if (Objects.equals(value, currentMasked)) {
            return UpdateDecision.UNCHANGED;
        }
        throw new IllegalArgumentException("Masked value does not match current value");
    }
}
