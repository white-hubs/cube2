package com.guhecloud.cube.base.flow.flowable.common.util;

import com.guhecloud.cube.base.dictionary.utils.BusinessConfigUtils;

/**
 * 流程引擎能力配置。
 */
public enum ProcessEngineAbility {

    /**
     * 转交同一审批节点，而不是仅转交当前任务。
     */
    TRANSFER_NODE;

    public static final String APPLICATION_ABILITY_CODE = "PROCESS_ENGINE_ABILITY";

    public boolean enabled() {
        return BusinessConfigUtils.abilityFlag(APPLICATION_ABILITY_CODE, name());
    }
}
