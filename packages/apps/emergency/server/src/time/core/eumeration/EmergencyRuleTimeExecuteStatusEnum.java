package com.guhecloud.cube.biz.emergency.server.time.core.eumeration;

import lombok.Getter;

@Getter
public enum EmergencyRuleTimeExecuteStatusEnum {

    /**
     * 执行中
     */
    PROCESS_IN,

    /**
     * 执行失败
     */
    PROCESS_FAIL,

    /**
     * 执行成功
     */
    PROCESS_SUCCESS;
}
