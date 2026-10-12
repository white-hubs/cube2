package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-14  11:16
 * @Description:
 * @Version: 1.0
 */
@Data
public class InspectionItemDetailsVO {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 巡检人员
     */
    private String userName;
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 完成时间
     */
    private LocalDateTime endTime;
    /**
     * 巡更项数
     */
    private Integer itemCount;
    /**
     * 正常项数
     */
    private Integer normalItemCount;
    /**
     * 异常项数
     */
    private Integer abnormalItemCount;
}
