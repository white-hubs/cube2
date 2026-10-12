package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration;

import com.guhecloud.platform.base.model.Pair;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/25 14:00
 * @Description:
 */
public enum TimeCycleTypeEnum {

    /**
     * 1个月
     */
    ONE_MONTH,

    /**
     * 1-3个月
     */
    ONE_TO_THREE_MONTH,

    /**
     * 3-6个月
     */
    THREE_TO_SIX_MONTH,

    /**
     * 6个月以上
     */
    SIX_MONTH,
    ;

    public static Pair<Integer, Integer> getEnumByValue(TimeCycleTypeEnum cycleTypeEnum) {
        if (cycleTypeEnum == null) {
            return Pair.of(null, null);
        }

        return switch (cycleTypeEnum) {
            case ONE_MONTH -> Pair.of(null, 1);
            case ONE_TO_THREE_MONTH -> Pair.of(1, 3);
            case THREE_TO_SIX_MONTH -> Pair.of(3, 6);
            case SIX_MONTH -> Pair.of(6, null);
            default -> Pair.of(null, null);
        };
    }
}
