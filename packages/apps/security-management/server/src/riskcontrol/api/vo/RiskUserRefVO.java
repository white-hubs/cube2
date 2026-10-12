package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

@Data
public class RiskUserRefVO {
    /**
     * 用户 ID。
     */
    private String userId;

    /**
     * 用户姓名，由 OUAA 用户服务解析得到。
     */
    private String userName;
}
