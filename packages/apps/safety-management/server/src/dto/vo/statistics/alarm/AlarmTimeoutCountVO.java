package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 超时告警数量统计
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
