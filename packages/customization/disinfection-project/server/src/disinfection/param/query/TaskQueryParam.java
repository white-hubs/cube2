package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/25 14:51
 * @Description:
 */
@Data
public class TaskQueryParam {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 任务类型
     */
    private Long taskTypeId;
    /**
     * 任务状态
     */
    private DisinfectionTaskStatusEnum status;
    /**
     * 关键字
     */
    private String keyword;

    /**
     * 时间
     */
    private Period period;

    /**
     * 所属人
     */
    private Long executorId;
}
