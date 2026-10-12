package com.guhecloud.cube.srv.gen.adapter.access.control.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.DasAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/das")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DasAccessController {

    private final DasAccessService accessControlService;

    /**
     * @Description 查询门禁点事件
     * @Author xiaofeng
     */
    @PostMapping("/getDoorEvents")
    public Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam doorEventsParam) throws Exception {
        return accessControlService.getDoorEvents(doorEventsParam);
    }

    /**
     * @Description 门禁点反控
     * @Author xiaofeng
     */
    @PostMapping("/doorControl")
    public List<DoorControlResultVo> doorControl(@RequestBody DoorControlParam param) {
        return accessControlService.doorControl(param);
    }
}
