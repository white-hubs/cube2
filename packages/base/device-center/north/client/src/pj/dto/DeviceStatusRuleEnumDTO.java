package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class DeviceStatusRuleEnumDTO {

    private List<Map<String, Object>> statusTypeList;


    private List<Map<String, Object>> valueTypeWithOperatorTypesList;

    public static DeviceStatusRuleEnumDTO buildFull() {
        DeviceStatusRuleEnumDTO vo = new DeviceStatusRuleEnumDTO();
        vo.setStatusTypeList(DeviceStatusEnum.toList());
        vo.setValueTypeWithOperatorTypesList(IotCompareType.supportedvalueTypeWithOperatorTypes());
        return vo;
    }
}
