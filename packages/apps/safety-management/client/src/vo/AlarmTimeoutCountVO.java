package com.guhecloud.cube.base.dataintegrate.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:35
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmTimeoutCountVO {
    /**
     * 告警总数
     */
    private Integer total;

    /**
     * 开启了超时配置的条数
     */
    private Integer timeoutConfigEnabled;

    /**
     * 开启配置并超时的条数
     */
    private Integer timeout;
}
