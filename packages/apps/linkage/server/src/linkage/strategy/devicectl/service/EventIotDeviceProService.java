package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.PropertyControlRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dto.LinkageIotDeviceProDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDevicePro;

import java.time.LocalDateTime;
import java.util.List;

public interface EventIotDeviceProService extends IService<EventIotDevicePro> {

    List<PropertyControlRequest> saveAndReturnEvent(Event event, Long linkageId, Long eventLinkageId,
                                                    List<LinkageIotDeviceProDTO> linkageIotDeviceProDTOS, LocalDateTime executeTime
                                                   );

    List<PropertyControlRequest> saveAndReturnEvent(EventIotDevicePro eventIotDevicePro);

    List<EventIotDevicePro> listByEventId(Long eventId);
}
