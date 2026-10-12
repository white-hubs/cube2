package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskAreaParam {
    /**
     * 区域编号；新增时未传则后端按 RA-{id} 生成，修改时未传保持原编号。
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
     * 区域类型。
     */
    private String areaType;

    /**
     * 区域 GIS 面数据 JSON。
     */
    private String gisGeometry;
}
