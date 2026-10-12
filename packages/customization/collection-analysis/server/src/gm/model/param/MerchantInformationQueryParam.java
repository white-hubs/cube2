package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户信息查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MerchantInformationQueryParam extends Paging {
    private Long id;
    private String name;
    /**
     * 商户类型（MerchantTypeEnum）
     */
    private String merchantType;

    /**
     * 经营类型(BusinessScopeEnum)
     */
    private String businessScope;
    /**
     * 采购车类型(VehicleTypeEnum)
     */
    private String vehicleType;

    /**
     * 经营规模范围(BusinessScaleScopeEnum)
     */
    private String businessScaleScope;
}
