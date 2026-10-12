package com.guhecloud.cube.iot.mgt.things.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class ProductPageParam extends Paging {
    private Long prodCategoryId;

    private String prodName;
}
