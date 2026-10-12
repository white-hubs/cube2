package com.guhecloud.cube.srv.gen.adapter.access.control.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorEventUploadRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.request.DoorStatusUploadRequest;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response.JieShunResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.helper.DoorRequesterHelper;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.JieShunAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.UnimplementedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 捷顺 门禁API
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JieShunAccessController {
    private final JieShunAccessService accessService;
    private final DoorRequesterHelper doorRequesterHelper;

    /**
     * Description 获取门禁点位分页列表
     * Author ht
     */
    @PostMapping("/js/getDoorPoints")
    public Page<AccessPointVo> getDoorPoints(@RequestBody AccessPointsParam accessPointsParam) {
        return accessService.getDoorPoints(accessPointsParam);
    }

    /**
     * Description 查询门禁设备列表
     * Author ht
     */

    @PostMapping("/js/getDoorDevices")
    public Page<DoorDevicesVo> getDoorDevices(@RequestBody AccessDevicesParam accessDevicesParam) {
        return accessService.getDoorDevices(accessDevicesParam);
    }

    /**
     * Description 查询门禁点事件
     * Author ht
     */
    @PostMapping("/js/getDoorEvents")
    public Page<DoorEventsVo> getDoorEvents(@RequestBody DoorEventsParam doorEventsParam) throws Exception {
        return accessService.getDoorEvents(doorEventsParam);
    }

    /**
     * Description 门禁点反控
     * Author ht
     */
    @PostMapping("/js/doorControl")
    public List<DoorControlResultVo> doorControl(@RequestBody DoorControlParam param) {
        return accessService.doorControl(param);
    }

    /**
     * Description 获取门禁状态
     * Author ht
     */
    @PostMapping("/js/getDoorStates")
    public List<AuthDoorStatusVo> getDoorStates(@RequestBody List<String> ids) {
        return accessService.getDoorStates(ids);
    }

    /**
     * Description 上传门禁事件
     * Author ht
     */
    @PostMapping("/public/js/{tenant}/doorrecord")
    public JieShunResponse<Void> uploadDoorEvent(@PathVariable Integer tenant, @Valid @RequestBody DoorEventUploadRequest request) {
        log.info("捷顺门禁事件上传,租户:{},请求参数:{}", tenant, request);
        //设置tenant到requester中，用于租户插件获取租户id
        Assert.isUnknownError(!doorRequesterHelper.rebuildTenant(tenant), "设置tenant失败");
        return accessService.uploadDoorEvent(request);
    }

    /**
     * Description 接收设备状态信息
     * Author ht
     */
    @PostMapping("/public/js/{tenant}/equipmentstatus")
    public JieShunResponse<Void> uploadDoorStatus(@PathVariable Integer tenant, @Valid @RequestBody List<DoorStatusUploadRequest> request) {
        throw new UnimplementedException("由物联服务提供设备状态，本服务暂不支持");
    }

}
