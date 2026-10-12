package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceQueryParam;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleConditionDeviceService;
import com.guhecloud.cube.biz.emergency.server.rule.application.EmergencyRuleService;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionDevicePO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyTypeDevicePageVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 预案设备接口管理
 *
 * @author lanchun
 * @date 2022/12/1 14:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyTypeDeviceController {

    private final SecurityDeviceClient securityDeviceClient;

    private final EmergencyTypeService emergencyTypeService;

    private final EmergencyRuleConditionDeviceService emergencyRuleConditionDeviceService;

    private final EmergencyRuleService emergencyRuleService;

    private final AreaClient areaClient;

    /**
     * 预案类型设备分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/emergency-type-devices")
    public Pagination<EmergencyTypeDevicePageVO> emergencyTypeDevicePage(@Valid EmergencyDeviceQueryParam param) {
        EmergencyType emergencyType = this.emergencyTypeService.getEmergencyTypeByAppCode(param.getApplicationCode());
        if (Objects.isNull(emergencyType)) {
            return Pagination.empty();
        }
        ModelUnderDeviceParam deviceParam = ObjectUtils.copying(param, ModelUnderDeviceParam.class);
        Pagination<SecurityDeviceVo> deviceMonitoringVoPagination = this.securityDeviceClient.getDeviceByModel(deviceParam);
        if (CollectionUtils.isEmpty(deviceMonitoringVoPagination.getRecords())) {
            return Pagination.empty();
        }
        //根据设备获取预案信息
        List<EmergencyTypeDevicePageVO> emergencyTypeDevicePageVOS = new ArrayList<>(deviceMonitoringVoPagination.getRecords().size());
        Set<Long> deviceIds = deviceMonitoringVoPagination.getRecords().stream().map(SecurityDeviceVo::getDeviceId)
                .collect(Collectors.toSet());
        List<EmergencyRuleConditionDevicePO> emergencyRuleConditionDevicePOS = emergencyRuleConditionDeviceService.listByDeviceIds(deviceIds);
        Set<Long> ruleIds = emergencyRuleConditionDevicePOS.stream()
                .map(EmergencyRuleConditionDevicePO::getEmergencyRuleId).collect(Collectors.toSet());
        //获取预案信息
        List<EmergencyRulePO> emergencyRulePOS = this.emergencyRuleService.listByIds(ruleIds);
        deviceMonitoringVoPagination.getRecords().forEach(deviceMonitoringVo -> {
            Set<Long> areaIds = Arrays.stream(deviceMonitoringVo.getAreaId().split("-")).map(Long::valueOf)
                    .collect(Collectors.toSet());
            Map<Long, String> areaIdNameMapping = areaClient.getByIds(areaIds).stream()
                    .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
            EmergencyTypeDevicePageVO emergencyTypeDevicePageVO = EmergencyTypeDevicePageVO.builder()
                    .id(deviceMonitoringVo.getDeviceId())
                    .code(deviceMonitoringVo.getCode())
                    .name(deviceMonitoringVo.getDeviceName())
                    .areaId(deviceMonitoringVo.getAreaId())
                    .deviceProdCategoryCode(deviceMonitoringVo.getDeviceProdCategoryCode())
                    .deviceProdCategoryCodeConcat(deviceMonitoringVo.getDeviceProdCategoryCodeConcat())
                    .deviceProdCategoryName(deviceMonitoringVo.getDeviceProdCategoryName())
                    .deviceProdId(deviceMonitoringVo.getDeviceProdId())
                    .deviceProdName(deviceMonitoringVo.getDeviceProdName())
                    .brandName(deviceMonitoringVo.getBrandName())
                    .build();
            Set<Long> deviceRuleIds = emergencyRuleConditionDevicePOS.stream()
                    .filter(emergencyRuleConditionDevicePO -> emergencyTypeDevicePageVO.getId()
                            .equals(emergencyRuleConditionDevicePO.getDeviceId()))
                    .map(EmergencyRuleConditionDevicePO::getEmergencyRuleId)
                    .collect(Collectors.toSet());
            List<String> deviceEmergencyRuleNames = emergencyRulePOS.stream()
                    .filter(emergencyRulePO -> deviceRuleIds.contains(emergencyRulePO.getId()))
                    .map(EmergencyRulePO::getName)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deviceEmergencyRuleNames)) {
                emergencyTypeDevicePageVO.setEmergencyConfigs(String.join(",", deviceEmergencyRuleNames));
            }
            List<String> areaNames = Arrays.stream(deviceMonitoringVo.getAreaId().split("-")).map(areaId -> {
                if (areaIdNameMapping.containsKey(Long.parseLong(areaId))) {
                    return areaIdNameMapping.get(Long.parseLong(areaId));
                }
                return "";
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(areaNames)) {
                emergencyTypeDevicePageVO.setAreaName(String.join("-", areaNames));
            }
            emergencyTypeDevicePageVOS.add(emergencyTypeDevicePageVO);
        });
        return new Pagination<>(deviceMonitoringVoPagination.getTotal(), deviceMonitoringVoPagination.getPages(), emergencyTypeDevicePageVOS);
    }
}
