package com.guhecloud.cube.biz.emergency.server.rule.domain.valueoabject;

import lombok.Getter;

/**
 * 预案报警推送配置
 *
 * @author lanchun
 * @date 2024/5/20 9:25
 */
@Getter
public class EmergencyPushConfig {

    /**
     * 是否推送到调度中心
     */
    private final Boolean notifyCommandDispatch;

    /**
     * 是否开启警告声音
     */
    private final Boolean turnWarningSound;

    public EmergencyPushConfig(Boolean notifyCommandDispatch, Boolean turnWarningSound) {
        this.notifyCommandDispatch = notifyCommandDispatch;
        this.turnWarningSound = turnWarningSound;
    }
}
