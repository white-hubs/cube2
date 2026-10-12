package com.guhecloud.cube.biz.mdata.server.merchant.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author lzy
 * @date 2021/10/20
 */
@Data
public class MerchantStatQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 是否为卖家
     */
    private Boolean seller;

    /**
     * 是否为买家
     */
    private Boolean buyer;

}
