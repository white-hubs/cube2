package com.guhecloud.cube.iot.mgt.core.data.lang;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 系统中特殊值定义
 */
public class SpecialValConst {

    /**
     * 设备事件阈值延迟<br/>
     * 报警延迟：发生报警后延迟xxx时间再次确认
     */
    public static final String DEVICE_EVENT_THRESHOLD_DELAY = "@dev_event_delay@";

    /**
     * 设备事件阈值间隔<br/>
     * 报警间隔：发生报警后间隔xxx时间再报警<br/>
     */
    public static final String DEVICE_EVENT_THRESHOLD_INTERVAL = "@dev_event_interval@";

    /**
     * 支持的设备事件阈值
     */
    public static final ImmutableCollection<String> SUPPORT_DEV_EVENT_THRESHOLD = ImmutableList.of(DEVICE_EVENT_THRESHOLD_DELAY, DEVICE_EVENT_THRESHOLD_INTERVAL);

    private SpecialValConst() {

    }

    /**
     * 是否为设备事件阈值
     *
     * @param key key
     *
     * @return 是否为设备事件阈值
     */
    public static boolean isDeviceEventThreshold(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }

        if (key.contains(":")) {
            String thresholdCode = key.split(":")[0];
            return SUPPORT_DEV_EVENT_THRESHOLD.contains(thresholdCode);
        }

        return SUPPORT_DEV_EVENT_THRESHOLD.contains(key);
    }

    /**
     * 构建事件阈值编码
     *
     * @param eventCode     事件编码
     * @param thresholdCode 阈值编码
     *
     * @return 事件阈值编码
     */
    public static String buildEventThresholdCode(String eventCode, String thresholdCode) {
        return thresholdCode + ":" + eventCode;
    }
}
