package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

@Data
public class RiskWarningSignParam {
    /**
     * 警示标志编号；新增时未传则后端按 RWS-{id} 生成，修改时未传保持原编号。
     */
    private String code;

    /**
     * 警示标志名称。
     */
    private String name;

    /**
     * 警示标志类型。
     */
    private String signType;

    /**
     * 图标文件 ID；文件场景码为 RISK::CONTROL_WARNING_SIGN_ICON。
     */
    private String iconFileId;

    /**
     * 启用状态，true 表示启用，false 表示停用；新增未传时默认启用。
     */
    private Boolean enabled;
}
