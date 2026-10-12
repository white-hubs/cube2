package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.p;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务打卡记录分页参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskCheckInPageParam extends Paging {
    private Long projectId;
    private Long taskId;
    private Long userId;
    private String userName;
}
