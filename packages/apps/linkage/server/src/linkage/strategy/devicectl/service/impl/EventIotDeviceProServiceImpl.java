package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.PropertyControlRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dao.EventIotDeviceProMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dto.LinkageIotDeviceProDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDevicePro;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDeviceProRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration.AttributeControlExecuteResultEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventIotDeviceProServiceImpl extends ServiceImpl<EventIotDeviceProMapper, EventIotDevicePro> implements EventIotDeviceProService {

    @Autowired
    private EventIotDeviceProRecordService eventIotDeviceProRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PropertyControlRequest> saveAndReturnEvent(@NonNull Event event, @NonNull Long linkageId, @NonNull Long eventLinkageId,
                                                           @NonNull List<LinkageIotDeviceProDTO> linkageIotDeviceProDTOS,
                                                           LocalDateTime executeTime
                                                          ) {
        //执行设备列表
        List<PropertyControlRequest> propertySettingRequests = new ArrayList<>();
        //执行设备记录
        List<EventIotDeviceProRecord> deviceProRecords = new ArrayList<>();
        //控制物联网的设备列表
        List<EventIotDevicePro> eventIotDevicePros = new ArrayList<>();
        Principal principal = SecurityContextHolder.getPrincipal(true);
        linkageIotDeviceProDTOS.stream().forEach(linkageIotDeviceProDTO -> {
            if (Objects.nonNull(linkageIotDeviceProDTO.getId())) {
                EventIotDevicePro eventIotDevicePro = EventIotDevicePro
                        .builder()
                        .deviceId(linkageIotDeviceProDTO.getId())
                        .deviceCode(linkageIotDeviceProDTO.getCode())
                        .deviceName(linkageIotDeviceProDTO.getName())
                        .deviceProdCategoryCode(linkageIotDeviceProDTO.getDeviceProdCategoryCode())
                        .deviceProdCategoryName(linkageIotDeviceProDTO.getDeviceProdCategoryName())
                        .deviceProdId(linkageIotDeviceProDTO.getDeviceProdId())
                        .deviceProdName(linkageIotDeviceProDTO.getDeviceProdName())
                        .linkageId(linkageId)
                        .eventId(event.getId())
                        .eventLinkageId(eventLinkageId)
                        .executeMode(linkageIotDeviceProDTO.getExecuteMode())
                        .propCode(linkageIotDeviceProDTO.getPropCode())
                        .propName(linkageIotDeviceProDTO.getPropName())
                        .propValue(linkageIotDeviceProDTO.getPropValue())
                        .build();
                //自动执行
                if (ExecuteModeEnum.AUTOMATIC == linkageIotDeviceProDTO.getExecuteMode()) {
                    //执行记录
                    EventIotDeviceProRecord eventIotDeviceProRecord = ObjectUtils.copying(eventIotDevicePro, EventIotDeviceProRecord.class);
                    eventIotDeviceProRecord.setId(IdWorker.getId());
                    eventIotDeviceProRecord.setOperatorBy(Objects.nonNull(principal) ? principal.getAccount() : null);
                    eventIotDeviceProRecord.setOperatorName(
                            Objects.nonNull(principal) && StringUtils.isNotEmpty(principal.getName()) ? principal.getName() : "系统");
                    eventIotDeviceProRecord.setExecuteStatus(AttributeControlExecuteResultEnum.ARRIVAL);
                    eventIotDeviceProRecord.setExecuteTime(executeTime);
                    eventIotDeviceProRecord.setOperatorTime(executeTime);
                    getCommandParam(propertySettingRequests, eventIotDeviceProRecord, linkageIotDeviceProDTO.getId(), linkageIotDeviceProDTO.getPropCode(), linkageIotDeviceProDTO.getPropValue());
                    deviceProRecords.add(eventIotDeviceProRecord);
                }
                eventIotDevicePros.add(eventIotDevicePro);
            }
        });
        this.saveBatch(eventIotDevicePros);
        this.eventIotDeviceProRecordService.saveBatch(deviceProRecords);
        return propertySettingRequests;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PropertyControlRequest> saveAndReturnEvent(@NonNull EventIotDevicePro eventIotDevicePro) {
        //执行设备列表
        List<PropertyControlRequest> propertySettingRequests = new ArrayList<>();
        LocalDateTime executeTime = DateUtils.now();
        //执行记录
        EventIotDeviceProRecord eventIotDeviceProRecord = ObjectUtils.copying(eventIotDevicePro, EventIotDeviceProRecord.class);
        eventIotDeviceProRecord.setId(IdWorker.getId());
        eventIotDeviceProRecord.setExecuteStatus(AttributeControlExecuteResultEnum.ARRIVAL);
        eventIotDeviceProRecord.setExecuteMode(ExecuteModeEnum.MANUAL);
        eventIotDeviceProRecord.setExecuteTime(executeTime);
        eventIotDeviceProRecord.setOperatorBy(SecurityContextHolder.getPrincipal().getAccount());
        eventIotDeviceProRecord.setOperatorName(SecurityContextHolder.getPrincipal().getName());
        eventIotDeviceProRecord.setOperatorTime(executeTime);
        getCommandParam(propertySettingRequests, eventIotDeviceProRecord, eventIotDevicePro.getDeviceId(), eventIotDevicePro.getPropCode(), eventIotDevicePro.getPropValue());
        this.eventIotDeviceProRecordService.save(eventIotDeviceProRecord);
        return propertySettingRequests;
    }

    private void getCommandParam(List<PropertyControlRequest> propertySettingRequests, EventIotDeviceProRecord eventIotDeviceProRecord,
                                 Long deviceId, String propCode, String propValue
                                ) {
        PropertyControlRequest propertySettingRequest = new PropertyControlRequest();
        propertySettingRequest.setCommandSeq(String.valueOf(eventIotDeviceProRecord.getId()));
        propertySettingRequest.setDeviceId(String.valueOf(deviceId));
        propertySettingRequest.setDeviceName(eventIotDeviceProRecord.getDeviceName());
        propertySettingRequest.setPropCode(propCode);
        propertySettingRequest.setPropValue(propValue);
        propertySettingRequests.add(propertySettingRequest);
    }

    @Override
    public List<EventIotDevicePro> listByEventId(@NonNull Long eventId) {
        return this.lambdaQuery().eq(EventIotDevicePro::getEventId, eventId).list();
    }
}
