package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 资产建档统计
 * @date 2023/10/10 16:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PropertyFilingNumQuery extends BaseQuery {
    /**
     * 资产状态
     */
    private String status;
    /**
     * 资产类别
     */
    private String propertyCategory;
    /**
     * 是否过期
     */
    private boolean expired;

}
