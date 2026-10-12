package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.BusinessScaleScopeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.BusinessScopeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.MerchantTypeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.SexEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.VehicleTypeEnum;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.MerchantStatDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.KeyValueResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.MerchantStatResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.CountryAddressMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.MerchantInformationMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.CountryAddressPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.MerchantInformationPO;
import com.guhecloud.cube.biz.collection.analysis.gm.service.MerchantStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户画像统计
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantStatisticsServiceImpl extends ServiceImpl<MerchantInformationMapper, MerchantInformationPO> implements MerchantStatisticsService {

    private final CountryAddressMapper countryAddressMapper;

    /**
     * 商户统计
     *
     * @return 统计
     */
    @Override
    public MerchantStatResult getMerchantStat() {
        MerchantStatResult result = new MerchantStatResult();
        Map<Long, String> addressMap = new HashMap<>();
        List<CountryAddressPO> addressList = countryAddressMapper.selectList(Wrappers.<CountryAddressPO>lambdaQuery()
                                                                                     .eq(CountryAddressPO::getPid, BigDecimal.ZERO.longValue())
                                                                            );
        if (!CollectionUtils.isEmpty(addressList)) {
            addressList.forEach(address -> addressMap.put(address.getId(), address.getName()));
        }
        result.setWholesaler(this.getMerchantDTO(MerchantTypeEnum.WHOLESALER.name(), addressMap));
        result.setPurchaser(this.getMerchantDTO(MerchantTypeEnum.PURCHASER.name(), addressMap));
        return result;
    }

    /**
     * 获取统计对象
     *
     * @param type       类型
     * @param addressMap 区域map
     *
     * @return 批发商/采购商对象
     */
    private MerchantStatDTO getMerchantDTO(String type, Map<Long, String> addressMap) {
        MerchantStatDTO result = new MerchantStatDTO();
        List<MerchantInformationPO> list = this.list(Wrappers.<MerchantInformationPO>lambdaQuery()
                                                             .eq(MerchantInformationPO::getMerchantType, type)
                                                    );
        if (CollectionUtils.isEmpty(list)) {
            return result.empty();
        }
        int total = list.size();
        result.setTotal(total);

        Map<String, Integer> sexMap = new HashMap<>();
        Map<String, Integer> ageMap = new HashMap<>();
        Map<String, Integer> oneMap = new HashMap<>();
        Map<String, Integer> towMap = new HashMap<>();
        Map<String, Integer> pieMap = new HashMap<>();
        list.forEach(po -> {
            if (MerchantTypeEnum.PURCHASER.name().equals(type)) {
                handlerPurchaserMap(po, sexMap, ageMap, oneMap, towMap, pieMap);
            } else if (MerchantTypeEnum.WHOLESALER.name().equals(type)) {
                handlerWholesalerMap(po, sexMap, ageMap, oneMap, towMap, pieMap, addressMap);
            }
        });
        BigDecimal totalDecimal = new BigDecimal(total);
        result.setSexList(this.getMapToList(sexMap, totalDecimal));
        result.setAgeList(this.getMapToList(ageMap, totalDecimal));
        result.setBarChartOneList(this.getMapToList(oneMap, totalDecimal));
        result.setBarChartTwoList(this.getMapToList(towMap, totalDecimal));
        result.setPieChartList(this.getMapToList(pieMap, totalDecimal));
        return result;
    }

    private void handlerWholesalerMap(MerchantInformationPO po, Map<String, Integer> sexMap, Map<String, Integer> ageMap,
                                      Map<String, Integer> oneMap, Map<String, Integer> towMap, Map<String, Integer> pieMap,
                                      Map<Long, String> addressMap
                                     ) {
        // 处理性别和年龄
        this.handlerSexAndAgeMap(po, sexMap, ageMap);
        // 处理籍贯(省)
        if (po.getNativePlaceProvinceId() != null) {
            this.handlerMapNumber(addressMap.get(po.getNativePlaceProvinceId()), oneMap);
        }
        // 处理上游来源(省)
        if (po.getUpstreamSourceProvinceId() != null) {
            this.handlerMapNumber(addressMap.get(po.getUpstreamSourceProvinceId()), towMap);
        }
        /* 处理业务规模 */
        Integer businessScaleNumber = po.getBusinessScaleNumber();
        if (businessScaleNumber != null) {
            String scaleName = BusinessScaleScopeEnum.FIVE_ABOVE.getDescribe();
            if (businessScaleNumber <= 3) {
                scaleName = BusinessScaleScopeEnum.THREE.getDescribe();
            } else if (businessScaleNumber <= 5) {
                scaleName = BusinessScaleScopeEnum.FIVE.getDescribe();
            }
            this.handlerMapNumber(scaleName, pieMap);
        }
    }

    private void handlerPurchaserMap(MerchantInformationPO po, Map<String, Integer> sexMap, Map<String, Integer> ageMap,
                                     Map<String, Integer> oneMap, Map<String, Integer> towMap, Map<String, Integer> pieMap
                                    ) {
        // 处理性别和年龄
        this.handlerSexAndAgeMap(po, sexMap, ageMap);
        // 处理区域
        this.handlerMapNumber(po.getArea(), oneMap);
        // 处理经营类型
        this.handlerMapNumber(BusinessScopeEnum.getDescribe(po.getBusinessScope()), towMap);
        // 处理采购车类型
        this.handlerMapNumber(VehicleTypeEnum.getDescribe(po.getVehicleType()), pieMap);
    }

    private void handlerSexAndAgeMap(MerchantInformationPO po, Map<String, Integer> sexMap, Map<String, Integer> ageMap) {
        this.handlerMapNumber(SexEnum.getDescribe(po.getSex()), sexMap);

        Integer age = po.getAge();
        if (age != null) {
            String ageName = "0-10岁";
            String ageStr = String.valueOf(age);
            if (ageStr.length() >= 2) {
                String befAge = ageStr.substring(0, ageStr.length() - 1);
                ageName = befAge + "0-" + (Integer.parseInt(befAge) * 10 + 10) + "岁";
            }
            this.handlerMapNumber(ageName, ageMap);
        }
    }

    private void handlerMapNumber(String keyName, Map<String, Integer> map) {
        if (StringUtils.isNotBlank(keyName)) {
            Integer scopeNumber = map.get(keyName);
            if (scopeNumber != null) {
                scopeNumber++;
                map.put(keyName, scopeNumber);
            } else {
                map.put(keyName, BigDecimal.ONE.intValue());
            }
        }
    }

    private List<KeyValueResult> getMapToList(Map<String, Integer> map, BigDecimal totalDecimal) {
        List<KeyValueResult> result = new ArrayList<>();
        map.forEach((k, v) -> {
            String val = new BigDecimal(v).multiply(new BigDecimal(100)).divide(totalDecimal, 2, RoundingMode.HALF_UP).toString();
            result.add(KeyValueResult.builder().key(k).value(val).build());
        });
        return result;
    }
}
