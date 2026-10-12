package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.cube.biz.daily.task.configuration.param.DailyItemResultRelationInsertParam;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskTypeEnum;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/13 15:23
 * @Description: 任务基础属性
 */
@Data
public class DailyBaseParam {
    /**
     * 日常任务类型
     */
    @NotBlank(message = "类型不能为空")
    private String dailyType;
    /**
     * 任务类型Id
     */
    private Long taskCategoryId;

    /**
     * 任务类型（普通、标准、对象）
     */
    private DailyTaskTypeEnum taskType;
    /**
     * 紧急程度id
     */
    private Long urgencyId;
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
     * 是否发放给在岗人员
     */
    private Boolean isOnDuty;

    /**
     * 完成进度
     */
    private BigDecimal requireProgress;

    /**
     * 是否保存临时任务项
     */
    private Boolean isSaveTempItem = false;

    /**
     * 是否是持续性任务
     */

    private Boolean continuity = false;

    /**
     * 附件集合
     */
    private List<Long> fileIds;
    /**
     * 标准任务、普通任务发放条件
     */
    private List<DailyParticipateConditionParam> participateList;
    /**
     * 所选任务项（和所选对象互斥）
     */
    private List<Long> taskItemIdList;

    public void setTaskItemIdList(List<Long> taskItemIdList) {
        if (CollectionUtils.isEmpty(taskItemIdList)) {
            return;
        }
        if (CollectionUtils.isEmpty(this.taskItemIdList)) {
            this.taskItemIdList = new ArrayList<>();
        }
        this.taskItemIdList.addAll(taskItemIdList);
    }

    /**
     * 手动添加的任务项
     */
    private List<DailyItemResultRelationInsertParam> noExistItemList;
    /**
     * 执行对象集合(包含对象的执行内容)
     */
    private List<DailyTargetStandardResultParam> targetResultList;

    /**
     * 发布人信息
     */
    private Long publisherId;
}
