package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.CountryAddressQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.CountryAddressResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.CountryAddressMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.CountryAddressPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.service.CountryAddressRepository;
import com.guhecloud.cube.biz.collection.analysis.gm.service.CountryAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 国家区域服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountryAddressServiceImpl extends ServiceImpl<CountryAddressMapper, CountryAddressPO> implements CountryAddressService {

    private final CountryAddressRepository countryAddressRepository;

    /**
     * 根据条件查询地址集合
     *
     * @param param 条件
     *
     * @return 集合
     */
    @Override
    public List<CountryAddressResult> getCountryAddressList(CountryAddressQueryParam param) {
        return countryAddressRepository.getCountryAddressList(param);
    }

    /**
     * 获取树形区域
     *
     * @param param 条件
     *
     * @return 树形区域
     */
    @Override
    public List<CountryAddressResult> getCountryAddressListTree(CountryAddressQueryParam param) {
        return countryAddressRepository.getCountryAddressListTree(param);
    }
}
