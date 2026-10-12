package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.controller;

import com.guhecloud.cube.biz.linkage.server.facade.iot.service.LinkageIotDeviceProService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDeviceProRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.vo.EventIotDeviceProRecordVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 联动物联设备执行记录列表
 *
 * @author lanchun
 * @date 2022/6/29 19:38
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventIotDeviceProRecordController {

    private final EventIotDeviceProRecordService eventIotDeviceProRecordService;

    private final LinkageIotDeviceProService linkageIotDeviceProService;

    /**
     * 根据事件获取物联设备执行记录
     *
     * @param eventId
     *
     * @return
     */
    @GetMapping("/event-iot-device-pro-records/{eventId}")
    public List<EventIotDeviceProRecordVO> listByEventId(@PathVariable("eventId") Long eventId) {
        List<EventIotDeviceProRecord> eventIotDeviceProRecords = this.eventIotDeviceProRecordService.listByEventId(eventId);
        if (CollectionUtils.isEmpty(eventIotDeviceProRecords)) {
            return Collections.emptyList();
        }
        List<EventIotDeviceProRecordVO> eventIotDeviceProRecordVOS = ObjectUtils.copying(eventIotDeviceProRecords, EventIotDeviceProRecordVO.class);
        eventIotDeviceProRecordVOS.stream().forEach(eventIotDeviceProRecordVO -> {
            String deviceAttributeTranslated = this.linkageIotDeviceProService.getDeviceAttributeTranslated(eventIotDeviceProRecordVO.getDeviceId(),
                                                                                                            eventIotDeviceProRecordVO.getPropCode(),
                                                                                                            eventIotDeviceProRecordVO.getPropValue());
            if (StringUtils.isNotEmpty(deviceAttributeTranslated)) {
                eventIotDeviceProRecordVO.setTranslatedValue(deviceAttributeTranslated);
            } else {
                eventIotDeviceProRecordVO.setTranslatedValue(eventIotDeviceProRecordVO.getPropValue());
            }
            eventIotDeviceProRecordVO.setExecuteStatusDescribe(eventIotDeviceProRecordVO.getExecuteStatus().getDescription());
        });
        return eventIotDeviceProRecordVOS;
    }
}
