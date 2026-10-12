package com.guhecloud.cube.base.message2.server.initialization.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户初始化
 *
 * @author lanchun
 * @date 2023/7/3 14:53
 */
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
