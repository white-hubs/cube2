package com.guhecloud.cube.biz.linkage.server.facade.iot.response;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.biz.linkage.server.facade.iot.enumeration.AttributePermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAttributeRunningResponse {

    /**
     * 属性id
     */
    private Long attributeId;
    /**
     * 属性code
     */
    private String attributeCode;
    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 读写权限
     */
    private AttributePermissionEnum permission;

    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 物联网平台报上来的原始值
     */
    private Object originalValue;

    /**
     * 业务平台翻译后的值
     */
    private String translatedValue;
    /**
     * 属性单位
     */
    private String attributeUnitName;

    private JSONArray dataConstraint;//直接用JSON吧
}
