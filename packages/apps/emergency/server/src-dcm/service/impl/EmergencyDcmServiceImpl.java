package com.guhecloud.cube.biz.emergency.dcm.service.impl;

import com.guhecloud.cube.base.alarm.client.SatAlarmLevelClient;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.biz.emergency.dcm.service.EmergencyDcmService;
import com.guhecloud.cube.biz.emergency.dcm.support.EmergencyEventTypeDcmHelper;
import com.guhecloud.cube.biz.emergency.dcm.support.EmergencyRuleDcmHelper;
import com.guhecloud.cube.biz.emergency.dcm.support.EmergencyTypeDcmHelper;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionExpService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionGroupService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleTimeService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyEventTypeService;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyTypeService;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductPageParam;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.DcmSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.guhecloud.platform.dcm.support.DcmSupport.applyDcmConfig;
import static com.guhecloud.platform.dcm.support.DcmSupport.executeWithTenant;
import static com.guhecloud.platform.dcm.support.DcmSupport.fetchRuntimeConfig;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyDcmServiceImpl implements EmergencyDcmService {

    private EmergencyTypeService emergencyTypeService;

    private EmergencyEventTypeService emergencyEventTypeService;

    private EmergencyRuleService emergencyRuleService;

    private EmergencyRuleConditionService emergencyRuleConditionService;

    private EmergencyRuleConditionGroupService emergencyRuleConditionGroupService;

    private EmergencyRuleConditionExpService emergencyRuleConditionExpService;

    private EmergencyRuleTimeService emergencyRuleTimeService;

    private SatAlarmLevelClient satAlarmLevelClient;

    private ProductClient productClient;

    private IotProductClient iotProductClient;

    @Override
    public List<RequestedConfigItem> fetchRuntimeEmergencyTypeConfig() {
        return fetchRuntimeConfig(emergencyTypeService, new EmergencyTypeDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> applyEmergencyTypeConfig(List<RequestedConfigItem> diffs2Apply) {
        return applyDcmConfig(diffs2Apply, emergencyTypeService, new EmergencyTypeDcmHelper());
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeEmergencyEventTypeConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> ret.addAll(fetchRuntimeConfig(emergencyEventTypeService, new EmergencyEventTypeDcmHelper())));
        return ret;
    }

    @Override
    public List<RequestedConfigItem> applyEmergencyEventTypeConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> applyDcmConfig(diffs2Apply, emergencyEventTypeService, new EmergencyEventTypeDcmHelper()));
        return diffs2Apply;
    }

    @Override
    public List<RequestedConfigItem> fetchRuntimeEmergencyRuleConfig(Long tenant) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () ->
                ret.addAll(fetchRuntimeConfig(emergencyRuleService, newEmergencyRuleDcmHelper())));
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RequestedConfigItem> applyEmergencyRuleConfig(List<RequestedConfigItem> diffs2Apply, Long tenant) {
        executeWithTenant(tenant, () -> {
            EmergencyRuleDcmHelper dcmHelper = newEmergencyRuleDcmHelper();
            DcmSupport.applyDcmConfig(diffs2Apply,
                                      emergencyRuleService::list,
                                      dcmHelper::batchAdd,
                                      dcmHelper::batchUpdate,
                                      dcmHelper::batchRemove,
                                      dcmHelper);
        });
        return diffs2Apply;
    }

    private EmergencyRuleDcmHelper newEmergencyRuleDcmHelper() {
        ProductPageParam productPageParam = new ProductPageParam();
        productPageParam.setPageNumber(1);
        productPageParam.setPageSize(1000);
        productPageParam.setWithEventSpec(true);
        List<ProductDTO> productDTOS = iotProductClient.page(productPageParam).getRecords();
        Set<Long> productIds = productDTOS.stream().map(ProductDTO::getProdId).map(Long::parseLong).collect(Collectors.toSet());
        return new EmergencyRuleDcmHelper(satAlarmLevelClient.listAlarmLevels(),
                                          emergencyTypeService.list(),
                                          productClient.getProductList(productIds),
                                          productDTOS,
                                          emergencyRuleService,
                                          emergencyRuleConditionGroupService,
                                          emergencyRuleConditionService,
                                          emergencyRuleConditionExpService,
                                          emergencyRuleTimeService);
    }
}
