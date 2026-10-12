package com.guhecloud.cube.biz.emergency.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantInitMessage {

    public final static String TOPIC = "TENANT_INIT";

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
