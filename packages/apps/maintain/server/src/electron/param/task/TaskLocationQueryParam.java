package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import lombok.Data;

@Data
public class TaskLocationQueryParam {
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 执行时段
     */
    private String executePeriod;
    /**
     * 区域编码
     */
    private String areaSequence;
    /**
     * 未执行对象
     */
    private Boolean resultNull;
}
