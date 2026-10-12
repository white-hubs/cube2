package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/26 18:09
 * @Description:
 */

@Data
@Builder
public class WholesalersDataJobParam {

    /**
     * 租户Id
     */
    private Integer tenant;
    /**
     * 采集开始时间
     */
    private LocalDateTime startTime;
    /**
     * 采集结束时间
     */
    private LocalDateTime endTime;
}
