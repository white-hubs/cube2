package com.guhecloud.cube.onecard.server.consumption.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/21 14:04
 * @Description: 消费采集参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OneCardJobParam {

    /**
     * 租户Id
     */
    private Integer tenant;
    /**
     * 采集开始时间
     */
    private String startTime;
    /**
     * 采集结束时间
     */
    private String endTime;
}
