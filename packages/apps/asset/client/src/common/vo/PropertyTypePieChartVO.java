package com.guhecloud.cube.property.management.client.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-17  14:16
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTypePieChartVO {
    /**
     * 资产总数
     */
    private Long total = 0L;
    /**
     * 选择的类型资产总数
     */
    private Integer typeTotal = 0;
    /**
     * 资产分类详情
     */
    private List<PieChartValueVO> details;

}
