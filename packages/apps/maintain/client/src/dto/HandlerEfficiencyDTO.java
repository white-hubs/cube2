package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/19 20:51
 * @Description:
 */
@Data
public class HandlerEfficiencyDTO {

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 名称
     */
    private String name;

    /**
     * 平均处理时间
     */
    private Long avgTime;
}
