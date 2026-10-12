package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskControlMeasureParam {
    /**
     * 管控措施 ID。已有措施编辑时必须回传，用于增量更新并保留执行状态。
     */
    private Long id;

    /**
     * 管控类型，例如工程技术、管理措施、培训教育、个体防护、应急处置。
     */
    private String controlType;

    /**
     * 管控层级。
     */
    private String controlLevel;

    /**
     * 措施执行人用户 ID。
     */
    private String executorUserId;

    /**
     * 管控措施内容。
     */
    private String content;
}
