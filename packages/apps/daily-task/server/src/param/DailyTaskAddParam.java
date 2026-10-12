package com.guhecloud.cube.biz.daily.task.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class DailyTaskAddParam extends DailyBaseParam {
    /**
     * 任务id
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;
    /**
     * 父级任务id
     */
    private Long parentId;

    /**
     * 计划Id
     */
    private Long planId;

    /**
     * 存草稿
     */
    private Boolean draft;

    /**
     * 任务用户
     */
    private Set<Long> taskUserIds;

    /**
     * 任务关联网格，仅服务端构建任务时使用
     */
    private Set<Long> gridAreaIds = new HashSet<>();

}
