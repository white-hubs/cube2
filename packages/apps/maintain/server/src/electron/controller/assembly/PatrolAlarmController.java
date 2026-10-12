package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.assembly;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolAlarmParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolAlarmService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolAlarmVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡检报警管理
 *
 * @author : appet
 * @date : 2023-10-19
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolAlarmController {
    private final PatrolAlarmService patrolAlarmService;

    /**
     * 巡更报警分页查询
     */
    @GetMapping("/patrol/alarm/page")
    public Pagination<PatrolAlarmVO> pagePatrolDevice(PatrolAlarmParam param) {
        return patrolAlarmService.getPage(param);
    }

}
