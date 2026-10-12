package com.guhecloud.cube.biz.collection.analysis.gm.controller;

import com.guhecloud.cube.biz.collection.analysis.gm.model.param.CountryAddressQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.CountryAddressResult;
import com.guhecloud.cube.biz.collection.analysis.gm.service.CountryAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 国家区域控制层（三级）
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountryAddressController {
    private final CountryAddressService countryAddressService;

    /**
     * 查询地址列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    @GetMapping("/country-address/list")
    public List<CountryAddressResult> getCountryAddressList(CountryAddressQueryParam param) {
        return countryAddressService.getCountryAddressList(param);
    }

    /**
     * 获取树形区域
     *
     * @param param 条件
     *
     * @return 树形区域
     */
    @GetMapping("/country-address/tree")
    public List<CountryAddressResult> getCountryAddressListTree(CountryAddressQueryParam param) {
        return countryAddressService.getCountryAddressListTree(param);
    }

}
