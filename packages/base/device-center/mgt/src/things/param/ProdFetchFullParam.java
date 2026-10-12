package com.guhecloud.cube.iot.mgt.things.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProdFetchFullParam extends Paging {
    private List<Long> productIds;

    /**
     * like %prodBrandName%
     */
    private String prodName;

    private Long prodCategoryId;

    private Long nodeType;

    private Long prodBrandId;

    private Long prodNumberId;

    private Long accessTypeId;

    private boolean withProdCategoryItem;

    private boolean withProdBrandItem;

    private boolean withAccessTypeItem;
}

