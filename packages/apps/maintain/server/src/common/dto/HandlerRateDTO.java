package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/19 20:17
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandlerRateDTO {
    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    private Long number;

    /**
     * 率
     */
    private Double rate;

    /**
     * 平均处理时间
     */
    private Double averageTime;
}
