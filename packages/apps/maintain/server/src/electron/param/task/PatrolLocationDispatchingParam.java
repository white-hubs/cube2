package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 任务巡更点分发参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrolLocationDispatchingParam {
    private Long taskId;
    private Long userId;
    private List<Long> locationIds;
}
