package com.guhecloud.cube.biz.linkage.client.validator.provider;

import java.util.List;

public interface ActionValidatorProvider {

    /**
     * 加入分组
     *
     * @param enabled
     * @param defaultGroupSequence
     *
     * @return
     */
    default void addGroupSequence(Boolean enabled, List<Class<?>> defaultGroupSequence) {
        if (enabled) {
            // 启用校验
            defaultGroupSequence.add(WhenEnabledTrueGroup.class);
        } else {
            defaultGroupSequence.add(WhenEnabledDefaultGroup.class);
        }
    }
}
