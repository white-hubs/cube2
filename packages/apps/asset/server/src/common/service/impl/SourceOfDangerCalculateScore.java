package com.guhecloud.cube.srv.gen.property.management.server.common.service.impl;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.PropertyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexTypeEnum;
import com.guhecloud.cube.srv.gen.riskscore.sdk.service.CalculateScoreService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.vo.propertyfiling.PropertyFilingRiskIndexVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 危险源建档指数
 * @date 2023/11/20 10:00
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SourceOfDangerCalculateScore implements CalculateScoreService<PropertyFilingRiskIndexVO> {
    private final RiskSourceLedgerService riskSourceLedgerService;
    private final RiskScoreCache riskScoreCache;

    @Override
    public RiskIndexTypeEnum calculateIndexType() {
        return RiskIndexTypeEnum.PROPERTY_FILING;
    }

    @Override
    public PropertyFilingRiskIndexVO calculateRiskScore() {
        PropertyFilingRiskIndexVO build = new PropertyFilingRiskIndexVO();
        List<SafetyManageCollectPropertyVO> voList = riskSourceLedgerService.queryAllByCreateTime();
        List<PropertyFilingRiskIndexVO.PropertyCategoryRiskIndex> riskIndexList = new ArrayList<>();
        Integer flammableEquipment = 5;
        Integer transportDevices = 5;
        Integer fireFightingEquipment = 10;
        if (ObjectUtils.isNotEmpty(voList)) {
            Map<String, Long> collect = voList.stream()
                    .collect(Collectors.groupingBy(SafetyManageCollectPropertyVO::getPropertyType, Collectors.counting()));
            flammableEquipment = ObjectUtils.isNotEmpty(collect.get(PropertyTypeEnum.FLAMMABLE_EQUIPMENT.name())) ? 0 : 5;
            transportDevices = ObjectUtils.isNotEmpty(collect.get(PropertyTypeEnum.TRANSPORT_DEVICES.name())) ? 0 : 5;
            fireFightingEquipment = ObjectUtils.isNotEmpty(collect.get(PropertyTypeEnum.FIRE_FIGHTING_EQUIPMENT.name())) ? 0 : 10;
        }
        riskIndexList.add(PropertyFilingRiskIndexVO.PropertyCategoryRiskIndex.builder()
                                  .propertyCategory(PropertyTypeEnum.FLAMMABLE_EQUIPMENT.name())
                                  .riskIndex(flammableEquipment).build());
        riskIndexList.add(PropertyFilingRiskIndexVO.PropertyCategoryRiskIndex.builder()
                                  .propertyCategory(PropertyTypeEnum.TRANSPORT_DEVICES.name())
                                  .riskIndex(transportDevices).build());
        riskIndexList.add(PropertyFilingRiskIndexVO.PropertyCategoryRiskIndex.builder()
                                  .propertyCategory(PropertyTypeEnum.FIRE_FIGHTING_EQUIPMENT.name())
                                  .riskIndex(fireFightingEquipment).build());
        build.setPropertyCategoryDetails(riskIndexList);
        build.setRiskIndex(flammableEquipment + transportDevices + fireFightingEquipment);
        build.setTenant(SessionContextHolder.getRequester().getTenant());
        riskScoreCache.settingRiskScoreInfo(RiskIndexTypeEnum.PROPERTY_FILING, build);
        log.info("危险源风险指数计算结果：{}", JSON.toJSONString(build));
        return build;
    }
}
