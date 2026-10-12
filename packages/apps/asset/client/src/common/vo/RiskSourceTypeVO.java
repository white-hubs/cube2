package com.guhecloud.cube.property.management.client.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 风险源类型统计
 * @date 2024/2/21 16:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiskSourceTypeVO {
    /**
     * 总数
     */
    private Long totalNum;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 类型编号
     */
    private String typeCode;
    /**
     * 类型id
     */
    private Long typeId;
    /**
     * 占比
     */
    private Float ratio;
}
