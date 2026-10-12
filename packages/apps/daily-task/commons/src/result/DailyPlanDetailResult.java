package com.guhecloud.cube.biz.daily.task.commons.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 计划返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanDetailResult {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;
    private String name;
    /**
     * 计划状态 DailyPlanStatusEnum
     */
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 任务类型（普通、标准、对象）
     */
    private String taskType;
    /**
     * 执行方式 DailyExecuteWayEnum
     * 多种以逗号隔开,空表示可以直接点击执行.
     */
    private String executeWay;
    /**
     * 表达式id
     */
    private Long cronId;
    private String cronName;
    /**
     * 指定类型 JobLoopTypeEnum
     */
    private String specifiedType;
    /**
     * 指定的日期(根据指定的类型 这里可能是 月/周/号 等)
     * 例如 specifiedDays的值为7时: specifiedType为MONTH(每月)这里就是7月; specifiedType为WEEK时这里就是周日
     */
    private String specifiedDays;
    /**
     * 指定的时间(这里只有HH:mm)
     */
    private String specifiedTime;
    /**
     * 上次执行时间
     */
    private LocalDateTime lastTime;
    /**
     * 下次执行时间
     */
    private LocalDateTime nextTime;
    /**
     * 执行次数
     */
    private Integer executionCount;
    /**
     * 任务类型id
     */
    private Long taskCategoryId;
    private String taskCategoryName;
    /**
     * 紧急程度id
     */
    private Long urgencyId;
    private String urgencyName;
    /**
     * 任务有效期
     */
    private Integer taskDay;
    private Integer taskHour;
    private Integer taskMinute;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件关联key
     * (不直接用id关联是因为用单独的key可以直接知道是否有文件,没有就不用去查询了)
     */
    private Long fileRefKey;
    private List<FileVO> fileList;

    /**
     * 附加数据
     */
    private String metadata;
    /**
     * 负责人类型 USER，DEPT
     */
    private String superintendentType;
    /**
     * 处理方式
     */
    private String method;

    /**
     * 是否发放给在岗人员
     */
    private Boolean isOnDuty;

    /**
     * 完成进度
     */
    private BigDecimal requireProgress;
    /**
     * 负责人
     */
    private List<DailyParticipateConditionResult> participateList;

    /**
     * 计划的执行内容
     */
    private List<DailyItemResultDTO> taskItemList;

    /**
     * 计划分类型的对象(包含执行内容)
     */
    private Map<String, List<DailyPlanTargetResult>> targetTypeResultMap;

    private LocalDateTime createTime;
    private String createBy;
    private String createByName;
}
