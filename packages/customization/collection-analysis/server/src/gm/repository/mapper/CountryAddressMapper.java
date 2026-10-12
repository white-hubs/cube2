package com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.CountryAddressPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 国家区域(三级,不分租户)
 */
@Mapper
@InterceptorIgnore(tenantLine = "on")
public interface CountryAddressMapper extends BaseMapper<CountryAddressPO> {

}
