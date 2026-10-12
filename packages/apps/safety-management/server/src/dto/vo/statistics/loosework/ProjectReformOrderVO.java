package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/19 16:24
 * @Description:
 */

@Data
public class ProjectReformOrderVO {

    /**
     * 工程整改单名称
     */
    private String processInsName;
    /**
     * 工程名称
     */
    private String projectScheduleName;

    /**
     * 工程监管id
     */
    private String projectScheduleId;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 租户code
     */
    private Integer marketTenant;

    /**
     * 状态
     */
    private String status;
}
