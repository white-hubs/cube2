package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 20:40
 * @Description: 整改单
 */
@Data
@Builder
public class ProjectReformOrderVO {

    /**
     * 工程整改单名称
     */
    private String processInsName;
    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程监管id
     */
    private String projectScheduleId;

    /**
     * 状态
     */
    private String status;
}
