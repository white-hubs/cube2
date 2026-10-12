package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 10:21
 * @Description: 工程整改返回实体
 */

@Data
public class ProjectReformVO extends ProcessInstanceDTO {

    /**
     * 小散工程id
     */
    private String projectScheduleId;

    /**
     * 小散工程名称
     */
    private String projectName;

    /**
     * 工单编号
     */
    private String reformNum;
}
