package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Data;

/**
 * @author lzy
 * @date 2021/10/20
 */
@Data
public class MerchantStatQueryParam {

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

    private Integer pageSize;

    private Integer pageNumber;

}
