package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskPageParam extends Paging {

    /**
     * 任务类型ID
     */
    private Long taskTypeId;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 执行人id
     */
    private Long executorId;
    /**
     * 任务状态
     */
    private DisinfectionTaskStatusEnum status;

    /**
     * 任务截止时间
     */
    private Period deadLinePeriod;
    /**
     * 关键字
     */
    private String keyword;
}
