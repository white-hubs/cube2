package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

@Data
public class RiskAreaVO {
    /**
     * 风险区域 ID。
     */
    private Long id;

    /**
     * 区域编号。
     */
    private String code;

    /**
     * 区域名称。
     */
    private String name;

    /**
     * 所属部门 ID。
     */
    private String departmentId;

    /**
     * 所属部门引用对象，由 departmentId 解析得到。
     */
    private RiskDepartmentRefVO department;

    /**
     * 区域范围描述。
     */
    private String rangeDesc;

    /**
     * 区域备注。
     */
    private String description;

    /**
     * 区域负责人用户 ID。
     */
    private String ownerUserId;

    /**
     * 区域负责人引用对象，由 ownerUserId 解析得到。
     */
    private RiskUserRefVO ownerUser;

    /**
     * 区域类型。
     */
    private String areaType;

    /**
     * 区域 GIS 面数据 JSON。
     */
    private String gisGeometry;

    /**
     * 区域固有风险评估等级，取该区域下风险单元固有风险等级的最高等级。
     * 取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String inherentRiskLevel;

    /**
     * 区域现状风险评估等级，取该区域下风险单元控制风险等级的最高等级。
     * 取值：MAJOR=重大风险，LARGE=较大风险，GENERAL=一般风险，LOW=低风险。
     */
    private String controlRiskLevel;

    /**
     * 区域当前风险等级，与 controlRiskLevel 保持一致，供通用列表字段使用。
     */
    private String riskLevel;

    /**
     * 该区域下风险单元数量。
     */
    private Long unitCount;
}
