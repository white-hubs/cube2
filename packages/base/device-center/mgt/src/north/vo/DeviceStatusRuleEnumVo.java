package com.guhecloud.cube.iot.mgt.north.vo;

import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotCompareType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DeviceStatusRuleEnumVo {
    private List<Map<String, Object>> statusTypeList;


    private List<Map<String, Object>> valueTypeWithOperatorTypesList;

    public static DeviceStatusRuleEnumVo buildFull() {
        DeviceStatusRuleEnumVo vo = new DeviceStatusRuleEnumVo();
        vo.setStatusTypeList(DeviceStatusEnum.toList());
        vo.setValueTypeWithOperatorTypesList(IotCompareType.supportedvalueTypeWithOperatorTypes());
        return vo;
    }
}