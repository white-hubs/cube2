package com.guhecloud.cube.srv.gen.adapter.access.control.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.HikIsapiAccessRecordService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 海康设备 ISAPI 门禁记录接口。 */
@RestController
@RequestMapping("/hik/isapi")
@RequiredArgsConstructor
public class HikIsapiAccessRecordController {

    private final HikIsapiAccessRecordService hikIsapiAccessRecordService;

    /** 查询门禁设备事件记录。 */
    @PostMapping("/getDoorEvents")
    public Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam param) {
        return hikIsapiAccessRecordService.getDoorEvents(param);
    }
}
