package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceDeviceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会议设备管理
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceDeviceController {

    private final ConferenceDeviceService conferenceDeviceService;

    /**
     * 会议设备查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/conference-device")
    public Pagination<ConferenceDeviceVo> getConferenceDeviceInfo(ConferenceDeviceQueryParam param) {
        return conferenceDeviceService.getConferenceDeviceInfo(param);
    }

    /**
     * 会议设备添加
     *
     * @param param
     */
    @PostMapping("/conference-device/insertConferenceDevice")
    public void insertConferenceDevice(@RequestBody ConferenceDeviceAddOrUpdateParam param) {
        conferenceDeviceService.insertConferenceDevice(param);
    }

    /**
     * 会议设备修改
     *
     * @param param
     */
    @PutMapping("/conference-device/updateConferenceDevice")
    public void updateConferenceDevice(@RequestBody ConferenceDeviceAddOrUpdateParam param) {
        conferenceDeviceService.updateConferenceDevice(param);
    }
}
