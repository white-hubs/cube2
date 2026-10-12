package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-17  09:26
 * @Description:
 * @Version: 1.0
 */
@Data
public class ParkSettleCategoryWeekDTO {
    /**
     * 经营品类编码
     */
    private String mainCateGory;
    /**
     * 经营品类名称
     */
    private String mainCateGoryName;
    /**
     * 货物总重量
     */
    private String netWeight;
    private LocalDateTime days;
    /**
     * 百分比
     */
    private String percent;
}
