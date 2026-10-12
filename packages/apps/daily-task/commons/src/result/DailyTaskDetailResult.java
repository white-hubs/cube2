package com.guhecloud.cube.biz.daily.task.commons.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyItemResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DailyTaskDetailResult {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;

    /**
     * 任务类型（普通、标准、对象）
     */
    private String taskType;

    /**
     * 日常任务名称
     */
    private String name;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 失效时间
     */
    private LocalDateTime deadline;
    /**
     * 任务类型id
     */
    private Long taskCategoryId;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 任务类型名称
     */
    private String taskCategoryName;
    /**
     * 紧急程度id
     */
    private Long urgencyId;

    /**
     * 紧急程度名称
     */
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
     * 处理方式
     */
    private String method;
    /**
     * 文件关联key
     * (不直接用id关联是因为用单独的key可以直接知道是否有文件,没有就不用去查询了)
     */
    private Long fileRefKey;
    private List<FileVO> fileList;

    /**
     * 负责人类型 USER，DEPT
     */
    private String superintendentType;
    /**
     * （草稿箱编辑时使用）
     */
    private List<DailyParticipateConditionResult> targetResultList;
    /**
     * 负责人
     */
    private List<DailySuperintendentDTO> participateList;

    /**
     * 计划的执行内容
     */
    private List<DailyItemResultDTO> taskItemList;

    /**
     * 普通任务执行结果
     */
    private DailyRecordResult normalTaskRecord;

    /**
     * 是否允许移交
     */
    private Boolean allowTransfer;

    /**
     * 是否允许分发
     */
    private Boolean allowDistribute;

    /**
     * 是否允许处理
     */
    private Boolean allowHandle;

    /**
     * 是否允许提交
     */
    private Boolean allowSubmit;

    /**
     * 是否发放给在岗人员
     */
    private Boolean isOnDuty;

    /**
     * 完成进度
     */
    private BigDecimal requireProgress;

    /**
     * 是否是持续性任务
     */
    private Boolean continuity;

    /**
     * 是否允许反馈
     */
    private Boolean allowFeedback;
}
