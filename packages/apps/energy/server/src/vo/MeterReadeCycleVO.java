package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

/**
 * @author lrj
 * @Description 抄表周期
 * @date 2022-07-04 15:12:36
 */
@Data
public class MeterReadeCycleVO {

    //
    private Long id;
    //执行时间
    private String workTime;
    //周期时间
    private String readeVulue;
    //抄表周期
    private String readeType;
    //任务id
    private String taskId;

}