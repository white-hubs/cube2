package com.guhecloud.cube.biz.collection.analysis.gm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.CountryAddressQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.CountryAddressResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.CountryAddressPO;

import java.util.List;

/**
 * 国家区域（三级）
 */
public interface CountryAddressService extends IService<CountryAddressPO> {
    /**
     * 根据条件查询地址集合
     *
     * @param param 条件
     *
     * @return 集合
     */
    List<CountryAddressResult> getCountryAddressList(CountryAddressQueryParam param);

    /**
     * 获取树形区域
     *
     * @param param 条件
     *
     * @return 区域
     */
    List<CountryAddressResult> getCountryAddressListTree(CountryAddressQueryParam param);
}
