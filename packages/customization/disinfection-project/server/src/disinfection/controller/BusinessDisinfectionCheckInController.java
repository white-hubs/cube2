package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.p.TaskCheckInPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionTaskCheckInService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskCheckInVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (消杀任务)打卡记录
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BusinessDisinfectionCheckInController {
    private final DisinfectionTaskCheckInService disinfectionTaskCheckInService;

    /**
     * 获取打卡记录
     *
     * @param param 分页条件
     *
     * @return 分页数据
     */
    @GetMapping("/disinfection/check-in/page")
    public Pagination<TaskCheckInVO> pageCheckIn(TaskCheckInPageParam param) {
        return disinfectionTaskCheckInService.pageCheckIn(param);
    }
}
