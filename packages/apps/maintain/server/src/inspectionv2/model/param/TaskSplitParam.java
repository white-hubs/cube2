package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

import java.util.List;

/**
 * 任务拆解入参对象
 */
@Data
public class TaskSplitParam {

    private Long taskId;

    private List<TaskReceiveUsersParam> taskReceiveUsersDTOList;
}
