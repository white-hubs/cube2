package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  16:22
 * @Description:
 * @Version: 1.0
 */
@Data
public class ParkSettleCategoryDayDetailsDTO {
    /**
     * 经营品类编码
     */
    private String mainCateGory;
    /**
     * 品类名称
     */
    private String mainCateGoryName;
    /**
     * 货物总重量
     */
    private String netWeight;
    /**
     * 货物总重量占比
     */
    private String percent;
}
