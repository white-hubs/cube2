package com.guhecloud.cube.biz.ouaa.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppVersionUpgradeMessage {

    public static final String TOPIC = "APP_VERSION_UPGRADE";

    /**
     * 版本主键
     */
    private Long id;
}
