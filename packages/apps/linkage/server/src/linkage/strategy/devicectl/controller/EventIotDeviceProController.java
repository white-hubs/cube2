package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.controller;

import com.guhecloud.cube.biz.linkage.server.facade.iot.service.LinkageIotDeviceProService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDevicePro;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.handler.LinkageIotDeviceHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.param.DeviceCtrlCommandParam;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.vo.EventIotDeviceProVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 联动物联设备管理接口
 *
 * @author lanchun
 * @date 2022/6/29 19:38
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventIotDeviceProController {

    private final EventIotDeviceProService eventIotDeviceProService;

    private final LinkageIotDeviceHandler linkageIotDeviceHandler;

    private final LinkageIotDeviceProService linkageIotDeviceProService;

    /**
     * 事件设备控制
     *
     * @param param
     */
    @PostMapping("/event-iot-device-pro/ctrl")
    public void deviceCtrl(@Valid @RequestBody DeviceCtrlCommandParam param) {
        linkageIotDeviceHandler.deviceCtrl(param);
    }

    /**
     * 事件手动设备控制
     *
     * @param eventId 事件id
     */
    @PostMapping("/event/{eventId}/iot-device-pro/hand/ctrl")
    public void deviceHandCtrlByEventId(@PathVariable("eventId") Long eventId) {
        linkageIotDeviceHandler.deviceHandCtrl(eventId);
    }

    /**
     * 根据事件获取物联设备列表
     *
     * @param eventId
     *
     * @return
     */
    @GetMapping("/event-iot-device-pros/{eventId}")
    public List<EventIotDeviceProVO> listByEventId(@PathVariable("eventId") Long eventId) {
        List<EventIotDevicePro> eventIotDevicePros = this.eventIotDeviceProService.listByEventId(eventId);
        if (CollectionUtils.isEmpty(eventIotDevicePros)) {
            return Collections.emptyList();
        }
        List<EventIotDeviceProVO> eventIotDeviceProVOS = ObjectUtils.copying(eventIotDevicePros, EventIotDeviceProVO.class);
        eventIotDeviceProVOS.stream().forEach(eventIotDeviceProVO -> {
            eventIotDeviceProVO.setTranslatedValue(eventIotDeviceProVO.getPropValue());
            String deviceAttributeTranslated = this.linkageIotDeviceProService.getDeviceAttributeTranslated(eventIotDeviceProVO.getDeviceId(),
                                                                                                            eventIotDeviceProVO.getPropCode(),
                                                                                                            eventIotDeviceProVO.getPropValue());
            if (StringUtils.isNotEmpty(deviceAttributeTranslated)) {
                eventIotDeviceProVO.setTranslatedValue(deviceAttributeTranslated);
            }
        });
        return eventIotDeviceProVOS;
    }
}
