package com.guhecloud.cube.biz.daily.task.commons.result;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/7 15:20
 * @Description:
 */
@Data
public class DailyTaskTargetRefResult {

    private Long taskId;
    private String taskName;

    private Long targetId;

    private String targetName;

    private String areaName;

    private String location;
}
