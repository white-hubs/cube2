package com.guhecloud.cube.biz.collection.analysis.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "scene_schedule_job")
public class SceneScheduleJobPO {

    private Long id;

    /**
     * 任务编号
     */
    private String code;

    /**
     * 任务名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 超时时间
     */
    private Integer timeout;

    /**
     * 启用状态
     */
    private Boolean enable;

    /**
     * 上次执行时间
     */
    private Long lastTriggerTime;

    /**
     * 下次执行时间
     */
    private Long nextTriggerTime;

    private Integer tenant;

    private Long deleted;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;
}
