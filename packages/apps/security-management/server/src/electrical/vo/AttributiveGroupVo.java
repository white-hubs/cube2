package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

import java.util.List;

@Data
public class AttributiveGroupVo {
    private Long id;

    private String applicationCode;
    /**
     * 分组名称
     */
    private String name;

    private String attributeCode;

    private String attributeName;

    private List<PropertyHistoryVo> propertyHistoryVos;
}
