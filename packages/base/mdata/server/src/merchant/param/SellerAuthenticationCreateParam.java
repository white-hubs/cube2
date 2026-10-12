package com.guhecloud.cube.biz.mdata.server.merchant.param;

import lombok.Data;

import java.util.List;

/**
 * 卖家认证新增信息
 */
@Data
public class SellerAuthenticationCreateParam {

    /**
     * 商品品类
     */
    private List<String> goodsCategoryIds;

}
