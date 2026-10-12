package com.guhecloud.cube.biz.emergency.dcm.support;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class EmergencyDcmSupport {
    public static final String PROVIDER_EMERGENCY = "EMERGENCY";
    public static final String VERSION = "1.0";
    public static final String CONFIG_EM_TYPE = "RuleType";
    public static final String CONFIG_EM_EVENT_TYPE = "EventType";
    public static final String CONFIG_EM_RULE = "Rule";

    /**
     * 以下为集中定义的Rci kw args key, 以消除无意引入的差异.
     */
    public static final String KW_KEY_APP_CODE = "appCode";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = Arrays.asList(
            CONFIG_EM_TYPE,
            CONFIG_EM_EVENT_TYPE,
            CONFIG_EM_RULE
                                                                         );

    private EmergencyDcmSupport() {
        // disable instantiation
    }
}
