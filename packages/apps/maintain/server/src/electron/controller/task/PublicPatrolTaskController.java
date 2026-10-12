package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.task;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolOverviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡更任务公共接口
 *
 * @author lanchun
 * @date 2025/1/3 15:03
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicPatrolTaskController {

    private final PatrolTaskController patrolTaskController;

    /**
     * 巡更任务概览
     *
     * @param param 查询参数
     *
     * @return 巡更任务概览
     */
    @GetMapping("/public/electron/task/overview")
    public PatrolOverviewVO overview(PatrolTaskPageParam param) {
        return patrolTaskController.overview(param);
    }
}
