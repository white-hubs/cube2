package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.constants;

import java.util.HashSet;
import java.util.Set;

public class AlarmMetricConstant {

    /**
     * 明火烟雾事件分类
     */
    public static final Set<String> FlAME_SMOG_EVENT_CODES = new HashSet<>();

    /**
     * 违停拥堵事件分类
     */
    public static final Set<String> WEI_TING_YONG_DU_EVENT_CODES = new HashSet<>();

    /**
     * 一级报警编号
     */
    public static final String ONE_LEVEL_CODE = "one_level";

    /**
     * 二级报警编号
     */
    public static final String TWO_LEVEL_CODE = "two_level";

    /**
     * 三级报警编号
     */
    public static final String THREE_LEVEL_CODE = "three_level";

    /**
     * 处理状态编码
     */
    public static final String HANDLER_STATUS_CODE = "ALARM_HANDLER";

    /**
     * 车辆违停
     */
    public static final String ILOR_EVENT_CODE = "manageEvent_clwt";

    /**
     * 交通拥堵
     */
    public static final String TC_EVENT_CODE = "manageEvent_jtyd";

    /**
     * 违规占用消防通道
     */
    public static final String UPFL_EVENT_CODE = "firecontrolEvent_wgzyxftd";

    /**
     * 水压报警
     */
    public static final String WP_EVENT_CODE = "firecontrolEvent_sybj";

    /**
     * 手报报警
     */
    public static final String MCP_EVENT_CODE = "firecontrolEvent_sbbj";

    /**
     * ups报警
     */
    public static final String UPS_EVENT_CODE = "rotatingEvent_upsbj";

    /**
     * 温湿度报警
     */
    public static final String WSD_EVENT_CODE = "rotatingEvent_wsdbj";

    /**
     * 电压报警
     */
    public static final String VAL_EVENT_CODE = "electricEvent_dybj";

    /**
     * 漏电报警
     */
    public static final String LCA_EVENT_CODE = "electricEvent_ldbj";

    /**
     * 温度报警
     */
    public static final String TEMPERATURE_EVENT_CODE = "electricEvent_wdbj";

    private AlarmMetricConstant() {

    }

    static {
        FlAME_SMOG_EVENT_CODES.add("firecontrolEvent_mhbj");
        FlAME_SMOG_EVENT_CODES.add("firecontrolEvent_ywbj");

        WEI_TING_YONG_DU_EVENT_CODES.add("manageEvent_clwt");
        WEI_TING_YONG_DU_EVENT_CODES.add("manageEvent_jtyd");
    }
}
