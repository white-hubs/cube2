package com.guhecloud.cube.iot.mgt.things.vo;

import lombok.Data;
import java.util.List;

/**
 * 产品方法批量操作VO
 */
@Data
public class ProductMethodBatchVo {

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 方法列表（传递的方法将覆盖现有方法）
     */
    private List<ProductMethodMgtVo> methods;
}
