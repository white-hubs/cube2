package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-17  11:44
 * @Description:
 * @Version: 1.0
 */
@Data
public class TransportWeekDTO {
    /**
     * 录入日期
     */
    private LocalDateTime enterDate;
    /**
     * 快驴转运件数
     */
    private Integer quickPieceCount;
    /**
     * 普采转运件数数
     */
    private Integer normalPieceCount;
    /**
     * 快驴车辆总数
     */
    private Integer quickCarSum;
    /**
     * 普采车辆总数
     */
    private Integer normalCarSum;
}
