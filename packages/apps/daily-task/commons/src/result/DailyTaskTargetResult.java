package com.guhecloud.cube.biz.daily.task.commons.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/18 10:19
 * @Description:
 */
@Data
public class DailyTaskTargetResult {

    /**
     * 对象ID
     */
    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 对象名称
     */
    private String targetName;

    /**
     * 对象类型
     */
    private String targetType;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否允许分派
     */
    private Boolean allowAssign;

    /**
     * 对象编码
     */
    private String targetCode;

    /**
     * 所属区域
     */
    private String areaName;

    /**
     * 详细地址
     */
    private String location;
    /**
     * 经纬度
     */
    private String targetLntAndLat;

    /**
     * 执行记录id
     */
    private Long recordId;

    /**
     * 执行人
     */
    private Long executorId;
    private String executorName;

    /**
     * 执行时间
     */
    private LocalDateTime executionTime;

    /**
     * 执行结果
     */
    private String executionResult;

    /**
     * 异常明细
     */
    private String exceptionDetail;

    /**
     * 处理权限
     */
    private Boolean allowedHande;

    /**
     * 对象关联用户规则
     */
    private List<DailyParticipateConditionResult> ruleList;
}
