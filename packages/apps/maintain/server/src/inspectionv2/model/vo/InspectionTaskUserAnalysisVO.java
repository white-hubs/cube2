package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectLevelEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 19:00
 * @Description:
 */

@Data
@Builder
public class InspectionTaskUserAnalysisVO {

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行人
     */
    private String taskExecuteUser;

    /**
     * 任务级别(紧急/普通
     */
    private InspectLevelEnum level;

    /**
     * 巡检类型名
     */
    private String typeName;

    /**
     * 执行状态：执行中 已结束 已停用
     */
    private InspectTaskStatusEnum status;

    /**
     * 发布人名
     */
    private String publisherName;

    /**
     * 配置巡检目标对象数
     */
    private Integer targetNum;

    /**
     * 记录完成率
     */
    private double recordCompletionRate;

    /**
     * 巡查结果
     */
    private String result;

}
