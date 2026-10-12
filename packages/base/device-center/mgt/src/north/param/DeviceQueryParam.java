
package com.guhecloud.cube.iot.mgt.north.param;


import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceQueryParam extends Paging {
    private String productId;
    private String deviceCode;
    private String prodCategoryCode;
    private String prodBrandCode;
    private String deviceNameLike;

    /**
     * 是否关联品牌与型号级联信息
     */
    private boolean withNumberAndBrandPath = false;
}
