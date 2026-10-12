package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author lanchun
 * @date 2024/9/14 14:06
 */
@Data
public class MedicalRuleNotifyConfigParam {

    /**
     * 时间
     */
    private Integer times;

    /**
     * 超限时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 通知通道
     */
    private String notifyChannels;
}
