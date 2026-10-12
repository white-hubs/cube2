package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.base.device.center.metadata.lang.AttributePermissionEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceAttributeRunningResponseVo {
    private String attributeCode;
    private String attributeName;
    private AttributePermissionEnum permission;
    private String dataType;
    private String attributeUnitName;
    private JSONArray dataConstraint;
    private Object originalValue;
    private String translatedValue;

}
