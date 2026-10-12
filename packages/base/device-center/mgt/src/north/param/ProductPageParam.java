package com.guhecloud.cube.iot.mgt.north.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductPageParam extends Paging {
    private String productId;
    private String productName;
    private String productCategoryName;
    private List<String> productCategoryCodes;
    private String productBrandCode;
    private String prodNumberCode;

    private boolean withEventSpec;
    private boolean withPropertySpec;
    private boolean withMethodSpec;
}
