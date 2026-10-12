package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import lombok.Data;

@Data
public class TaskRateResult {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务对象总数
     */
    private Integer targetAllNum;

    /**
     * 执行人名称
     */
    private String receiveNames;

    /**
     * 执行进度
     */
    private Double rate;

    /**
     * 执行状态
     */
    private InspectTaskStatusEnum status;
}
