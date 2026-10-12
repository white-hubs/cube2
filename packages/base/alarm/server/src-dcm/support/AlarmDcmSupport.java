package com.guhecloud.cube.base.alarm.dcm.support;

import java.util.Arrays;
import java.util.List;

public class AlarmDcmSupport {
    public static final String PROVIDER_ALARM = "ALARM";

    public static final String CONFIG_ALARM_REASON = "AlarmReason";

    // 依据依赖顺序确定的偏序集
    public static final List<String> CONFIG_TYPES_ORDERED = Arrays.asList(
            CONFIG_ALARM_REASON
                                                                         );

    private AlarmDcmSupport() {
        // disable instantiation
    }
}
