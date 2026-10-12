package com.guhecloud.cube.srv.gen.safety.management.constant;

import com.guhecloud.cube.srv.gen.safety.management.enumeration.AlarmStatusEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlarmConstants {
    /**
     * 未确认的告警状态集
     */
    public static final List<String> UN_CONFIRMED_STATUS = new ArrayList<>(Collections.singleton(AlarmStatusEnum.ALARM_UN_CONFIRMED.name()));

    /**
     * 已处理的告警状态集
     */
    public static final List<String> HANDLED_STATUS = new ArrayList<>(Collections.singleton(AlarmStatusEnum.ALARM_HANDLER.name()));
}
