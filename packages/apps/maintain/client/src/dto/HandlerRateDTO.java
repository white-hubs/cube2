package com.guhecloud.cube.srv.gen.maintain.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HandlerRateDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    private Long num;

    /**
     * 率
     */
    private Double rate;
}
