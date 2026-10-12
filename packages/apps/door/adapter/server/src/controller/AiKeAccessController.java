package com.guhecloud.cube.srv.gen.adapter.access.control.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl.AiKeAccessServiceImpl;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 目前从文档中看不出厂商
 * 只是适用南沙项目
 */
@Slf4j
@RestController
@RequestMapping("/aike")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AiKeAccessController {
    private final AiKeAccessServiceImpl accessService;

    /**
     * @Description 获取门禁点位分页列表
     * @Author lrj
     */
    @PostMapping("/getDoorPoints")
    public Page<AccessPointVo> getDoorPoints(@RequestBody AccessPointsParam accessPointsParam) {
        return accessService.getDoorPoints(accessPointsParam);
    }

    /**
     * @Description 查询门禁设备列表
     * @Author lrj
     */
    @PostMapping("/getDoorDevices")
    public Page<DoorDevicesVo> getDoorDevices(@RequestBody AccessDevicesParam accessDevicesParam) {
        return accessService.getDoorDevices(accessDevicesParam);
    }

    /**
     * @Description 查询门禁点事件
     * @Author lrj
     */
    @PostMapping("/getDoorEvents")
    public Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam doorEventsParam) throws Exception {
        return accessService.getDoorEvents(doorEventsParam);
    }

    /**
     * @Description 门禁点反控
     * @Author lrj
     */
    @PostMapping("/doorControl")
    public List<DoorControlResultVo> doorControl(@RequestBody DoorControlParam param) {
        Assert.isParameterError(StringUtils.isEmpty(param.getOperator()), "操作人不能为空");
        return accessService.doorControl(param);
    }

    /**
     * @Description 获取门禁状态
     * @Author lrj
     */
    @PostMapping("/getDoorStates")
    public List<AuthDoorStatusVo> getDoorStates(@RequestBody List<String> ids) {
        return accessService.getDoorStates(ids);
    }
}
