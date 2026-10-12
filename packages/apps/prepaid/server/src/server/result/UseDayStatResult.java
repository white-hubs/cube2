package com.guhecloud.cube.srv.prepaid.server.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日用能统计返回对象
 */
@Data
public class UseDayStatResult {
    /**
     * 日期
     */
    private LocalDateTime useDate;
    /**
     * 用能
     */
    private String pv;
}
