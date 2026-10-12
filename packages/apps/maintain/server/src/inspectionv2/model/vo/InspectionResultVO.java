package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 巡检结果统计
 * @date 2023/4/14 14:35
 */
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class InspectionResultVO {
    /**
     * 巡检任务id
     */
    private Long id;
    /**
     * 稽查任务id
     */
    private Long checkTaskId;
    /**
     * 巡检任务名称
     */
    private String name;
    /**
     * 巡检对象数
     */
    private Integer targetNum;
    /**
     * 巡检类型
     */
    private String typeName;
    /**
     * 巡检人员
     */
    private String inspectionStaff;
    /**
     * 稽查人员
     */
    private String checkStaff;
    /**
     * 巡检结果
     */
    private String checkResult;
    /**
     * 稽查结果匹配率
     */
    private String checkMatchRatio;
    /**
     * 稽查不匹配次数
     */
    private Integer checkNotMatchNum;
    /**
     * 稽查不匹配项ids
     */
    private List<String> notMatchIds;
    /**
     * 异常隐患数
     */
    private Integer hiddenDangerNum;
    /**
     * 巡检保修数
     */
    private Integer faultNum;
    /**
     * 巡检耗时
     */
    private String elapsedTime;
    /**
     * 创建时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 是否为拆解任务
     */
    private Boolean isSplit;

}
