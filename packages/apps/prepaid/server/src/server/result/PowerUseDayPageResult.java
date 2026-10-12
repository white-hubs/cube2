package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 日用电分页返回对象
 */
@Data
public class PowerUseDayPageResult {
    private Long id;
    /**
     * 电表编号
     */
    private String meterId;
    /**
     * 日期
     */
    private LocalDateTime useDate;
    /**
     * 本段时间内的用电量
     */
    private String pv;
    /**
     * 剩余金额
     */
    private BigDecimal powerRemain;
    /**
     * 本段时间内的尖电能用电量
     */
    private String pvj;
    /**
     * 本段时间内的峰电能用电量
     */
    private String pvf;
    /**
     * 本段时间内的平电能用电量
     */
    private String pvp;
    /**
     * 本段时间内的谷电能用电量
     */
    private String pvg;
    /**
     * 当前抄表用电量
     */
    private String currentPv;
    /**
     * 当前抄表尖电量
     */
    private String currentPvj;
    /**
     * 当前抄表峰电量
     */
    private String currentPvf;
    /**
     * 当前抄表平电量
     */
    private String currentPvp;
    /**
     * 当前抄表谷电量
     */
    private String currentPvg;
    /**
     * 上次抄表用电量
     */
    private String lastPv;
    /**
     * 上次抄表尖电量
     */
    private String lastPvj;
    /**
     * 上次抄表峰电量
     */
    private String lastPvf;
    /**
     * 上次抄表平电量
     */
    private String lastPvp;
    /**
     * 上次抄表谷电量
     */
    private String lastPvg;
}
