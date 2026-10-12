package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.assembly;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolDeviceParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolDeviceService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡检设备管理
 *
 * @author : appet
 * @date : 2023-10-19
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolDeviceController {
    private final PatrolDeviceService patrolDeviceService;

    /**
     * 巡更设备分页查询
     */
    @GetMapping("/patrol/device/page")
    public Pagination<PatrolDeviceVO> pagePatrolDevice(PatrolDeviceParam param) {
        return patrolDeviceService.getPage(param);
    }

}
