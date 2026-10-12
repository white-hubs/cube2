package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskIdentificationObjectParam {
    /**
     * 风险对象 ID；为空表示新增对象，有值表示修改或删除已有对象。
     */
    private Long objectId;

    /**
     * 风险对象编号；新增时未传则后端按 RO-{id} 生成，修改时未传保持原编号。
     */
    private String code;

    /**
     * 风险对象名称。
     */
    private String name;

    /**
     * 资产管理服务资产 ID；风险对象仅保存外部资产引用。
     */
    private Long assetId;

    /**
     * 责任人用户 ID。
     */
    private String responsibleUserId;

    /**
     * 危险源或风险描述。
     */
    private String hazardDesc;

    /**
     * 事故类型，多个类型按前后端约定编码保存。
     */
    private String accidentTypes;

    /**
     * 事故原因。
     */
    private String accidentCause;

    /**
     * 触发因素。
     */
    private String triggerFactor;

    /**
     * 应急处置措施。
     */
    private String emergencyMeasure;

    /**
     * 是否删除该风险对象，true 表示本次提交删除。
     */
    private Boolean deleted;

    /**
     * 辨识人员用户 ID。
     */
    private String userId;
}
