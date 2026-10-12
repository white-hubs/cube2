package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/19 20:51
 * @Description:
 */
@Data
public class HandlerEfficiencyDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 平均处理时间
     */
    private Long avgTime;
}
