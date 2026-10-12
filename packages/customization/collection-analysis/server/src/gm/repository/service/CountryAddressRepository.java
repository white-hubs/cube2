package com.guhecloud.cube.biz.collection.analysis.gm.repository.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.CountryAddressQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.CountryAddressResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.CountryAddressMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.CountryAddressPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 国家区域
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CountryAddressRepository extends ServiceImpl<CountryAddressMapper, CountryAddressPO> {

    /**
     * 根据条件查询地址集合
     *
     * @param param 条件
     *
     * @return 集合
     */
    public List<CountryAddressResult> getCountryAddressList(CountryAddressQueryParam param) {
        List<CountryAddressPO> list = this.list(Wrappers.<CountryAddressPO>lambdaQuery()
                                                        .eq(param.getId() != null, CountryAddressPO::getId, param.getId())
                                                        .eq(param.getPid() != null, CountryAddressPO::getPid, param.getPid())
                                                        .le(param.getDeep() != null, CountryAddressPO::getDeep, param.getDeep())
                                                        .eq(StringUtils.isNotBlank(param.getName()) &&
                                                            Boolean.TRUE.equals(param.getEqQuery()), CountryAddressPO::getName, param.getName())
                                                        .like(StringUtils.isNotBlank(param.getName()), CountryAddressPO::getName, param.getName())
                                                        .eq(param.getExtId() != null, CountryAddressPO::getExtId, param.getExtId())
                                                        .eq(StringUtils.isNotBlank(param.getExtName()) &&
                                                            Boolean.TRUE.equals(param.getEqQuery()), CountryAddressPO::getExtName, param.getExtName())
                                                        .like(StringUtils.isNotBlank(param.getExtName()), CountryAddressPO::getName, param.getExtName())
                                               );
        return ConvertUtils.toList(CountryAddressResult.class, list);
    }

    /**
     * 获取树形区域
     *
     * @return 树形区域
     */
    public List<CountryAddressResult> getCountryAddressListTree(CountryAddressQueryParam param) {
        List<CountryAddressResult> result = new ArrayList<>();
        List<CountryAddressResult> list = this.getCountryAddressList(param);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        Map<Long, List<CountryAddressResult>> pidMap = list.stream().collect(Collectors.groupingBy(CountryAddressResult::getPid));
        Map<Long, List<CountryAddressResult>> idMap = list.stream().collect(Collectors.groupingBy(CountryAddressResult::getId));
        List<CountryAddressResult> parentList = list.stream().filter(c -> idMap.get(c.getPid()) == null).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(parentList)) {
            return result;
        }
        result.addAll(parentList);
        for (CountryAddressResult countryAddressResult : result) {
            loopChild(countryAddressResult, pidMap);
        }

        return result;
    }

    private void loopChild(CountryAddressResult parent, Map<Long, List<CountryAddressResult>> map) {
        List<CountryAddressResult> childList = map.get(parent.getId());
        if (CollectionUtils.isEmpty(childList)) {
            return;
        }
        parent.setChildList(childList);
        for (CountryAddressResult countryAddressResult : parent.getChildList()) {
            loopChild(countryAddressResult, map);
        }
    }

}
