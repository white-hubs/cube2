package com.guhecloud.cube.biz.collection.analysis.gm.model.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoodSafeStatisticsResult {
    private Long id;
    private LocalDateTime timeDay;
    private String timeDayFormat;
    /**
     * 检测次数
     */
    private Integer testCount;
    /**
     * 合格次数
     */
    private Integer passCount;
    /**
     * 合格率(例如：99.99)
     */
    private String passPercent;
}
