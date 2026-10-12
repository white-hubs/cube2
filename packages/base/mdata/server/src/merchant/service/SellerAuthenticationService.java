package com.guhecloud.cube.biz.mdata.server.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.SellerAuthentication;
import com.guhecloud.cube.biz.mdata.server.merchant.param.SellerAuthenticationCreateParam;

import java.util.List;

public interface SellerAuthenticationService extends IService<SellerAuthentication> {

    /**
     * 卖家认证新增
     *
     * @param param 认证新增信息
     */
    void save(SellerAuthenticationCreateParam param);

    /**
     * 卖家重新认证
     */
    void sellerReset();

    /**
     * 根据商户主键获取卖家认证信息
     *
     * @param merchantId 商户主键
     *
     * @return 卖家认证信息
     */
    SellerAuthentication getByMerchantId(Long merchantId);

    /**
     * 根据商户主键获取卖家认证信息集合
     *
     * @param merchantIds 商户主键集合
     *
     * @return 卖家认证信息集合
     */
    List<SellerAuthentication> getByMerchantIds(List<Long> merchantIds);
}
