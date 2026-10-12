package com.guhecloud.cube.property.management.client.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wql
 * @describe 风险源分析
 * @date 2024/2/21 16:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskSourceVO {
    /**
     * 风险源总数
     */
    private Long totalNum;
    /**
     * 风险源过期总数
     */
    private Long expirationNum;
    /**
     * 各风险源类型统计
     */
    private List<RiskSourceTypeVO> typeVOList;

}
