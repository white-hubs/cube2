package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日用水分页返回对象
 */
@Data
public class WaterUseDayPageResult {
    private Long id;
    /**
     * 水表编号
     */
    private String meterId;
    /**
     * 日期
     */
    private LocalDateTime useDate;
    /**
     * 剩余水量
     */
    private String powerRemain;
    /**
     * 本段时间内的用水量
     */
    private String pv;
    /**
     * 当前抄表用水量
     */
    private String currentPv;
    /**
     * 上次抄表用水量
     */
    private String lastPv;
}
