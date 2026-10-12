package com.guhecloud.cube.biz.mdata.server.stall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantStall;

import java.util.List;

/**
 * @author lzy
 * @date 2021/10/26
 */
public interface MerchantStallService extends IService<MerchantStall> {

    List<MerchantStall> getStallByMerchantId(Long merchantId);

}
