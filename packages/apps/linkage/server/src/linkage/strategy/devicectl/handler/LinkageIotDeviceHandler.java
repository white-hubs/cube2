package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.facade.device.event.PropertyControlEvent;
import com.guhecloud.cube.biz.linkage.server.facade.device.service.LinkageDeviceControlService;
import com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust.PropertyControlRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.param.LinkageActionModifyParam;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.LinkageActionAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.dto.LinkageIotDeviceProDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDevicePro;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration.AttributeControlExecuteResultEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.param.DeviceCtrlCommandParam;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.rocketmq.message.LinkageDeviceCtlMessage;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.rocketmq.message.LinkageDeviceMessage;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service.EventIotDeviceProService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionValidRequest;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommandResponse;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 物联设备动作
 *
 * @author lanchun
 * @date 2022/6/23 16:16
 */
@Slf4j
@Component
public class LinkageIotDeviceHandler extends LinkageActionAbstractHandler {

    @Autowired
    private EventIotDeviceProService eventIotDeviceProService;

    @Autowired
    private EventIotDeviceProRecordService eventIotDeviceProRecordService;

    @Autowired
    private LinkageDeviceControlService linkageDeviceControlService;

    @Autowired
    public LinkageIotDeviceHandler() {
        super(LinkageActionTypeEnum.IOT_DEVICE_ACTION);
    }

    public List<LinkageIotDeviceProDTO> buildActions(Object o) {
        return JsonUtils.deserialize(o, new TypeReference<List<LinkageIotDeviceProDTO>>() {
        });
    }

    /**
     * 通过规则json获取手动执行规则
     *
     * @param iotDeviceActionStr
     *
     * @return
     */
    public List<LinkageIotDeviceProDTO> manualActions(String iotDeviceActionStr) {
        return this.buildActions(iotDeviceActionStr).stream()
                .filter(linkageIotDeviceProDTO -> ExecuteModeEnum.MANUAL == linkageIotDeviceProDTO.getExecuteMode())
                .collect(Collectors.toList());
    }

    /**
     * 通过规则json获取自动执行规则
     *
     * @param iotDeviceActionStr
     *
     * @return
     */
    public List<LinkageIotDeviceProDTO> automaticActions(String iotDeviceActionStr) {
        return this.buildActions(iotDeviceActionStr).stream()
                .filter(linkageIotDeviceProDTO -> ExecuteModeEnum.AUTOMATIC == linkageIotDeviceProDTO.getExecuteMode())
                .collect(Collectors.toList());
    }

    /**
     * 解析所有的iot规则
     *
     * @param linkageEmergencyActionModifies
     *
     * @return
     */
    public List<LinkageIotDeviceProDTO> buildActions(List<LinkageActionModifyParam> linkageEmergencyActionModifies) {
        List<LinkageIotDeviceProDTO> iotDeviceProDTOS = new ArrayList<>();
        //解析物联设备动作
        if (!CollectionUtils.isEmpty(linkageEmergencyActionModifies)) {
            linkageEmergencyActionModifies.stream()
                    .filter(linkageEmergencyActionModifyParam -> LinkageActionTypeEnum.IOT_DEVICE_ACTION ==
                                                                 linkageEmergencyActionModifyParam.getActionType())
                    .forEach(linkageEmergencyActionModifyParam -> {
                        String iotDeviceActionStr = JsonUtils.serialize(linkageEmergencyActionModifyParam.getActionConfig());
                        iotDeviceProDTOS.addAll(this.buildActions(iotDeviceActionStr));
                    });
        }
        return iotDeviceProDTOS;
    }

    /**
     * 获取手动规则
     *
     * @param linkageEmergencyActionModifies
     *
     * @return
     */
    public List<LinkageIotDeviceProDTO> manualActions(List<LinkageActionModifyParam> linkageEmergencyActionModifies) {
        return this.buildActions(linkageEmergencyActionModifies).stream()
                .filter(linkageIotDeviceProDTO -> ExecuteModeEnum.MANUAL == linkageIotDeviceProDTO.getExecuteMode())
                .collect(Collectors.toList());
    }

    /**
     * 获取自动执行规则
     *
     * @param linkageEmergencyActionModifies
     *
     * @return
     */
    public List<LinkageIotDeviceProDTO> automaticActions(List<LinkageActionModifyParam> linkageEmergencyActionModifies) {
        return this.buildActions(linkageEmergencyActionModifies).stream()
                .filter(linkageIotDeviceProDTO -> ExecuteModeEnum.AUTOMATIC == linkageIotDeviceProDTO.getExecuteMode())
                .collect(Collectors.toList());
    }

    /**
     * 设备控制动作执行
     *
     * @param request
     */
    @Override
    public ExecuteStatusEnum eventActionExecute(LinkageActionExecuteRequest request) {
        AttributeControlExecuteResultEnum attributeControlExecuteResultEnum = AttributeControlExecuteResultEnum.ARRIVAL;
        ExecuteStatusEnum executeStatusEnum = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        List<PropertyControlRequest> list = new ArrayList<>();
        try {
            if (Objects.isNull(request.getLinkageActionDetailRequest().getActionConfig())) {
                //动作执行结果
                return executeStatusEnum;
            }
            LocalDateTime executeDate = DateUtils.now();
            //获取所有待执行的设备
            List<LinkageIotDeviceProDTO> linkageIotDeviceProDTOS = this.buildActions(request.getLinkageActionDetailRequest()
                                                                                             .getActionConfig());
            //执行设备入库
            list = this.eventIotDeviceProService.saveAndReturnEvent(request.getEvent(), request.getLinkageActionDetailRequest()
                    .getLinkageId(), request.getLinkageActionDetailRequest().getEventLinkageId(), linkageIotDeviceProDTOS, executeDate);
            //发布一组设备执行命令到物联网平台
            this.linkageDeviceControlService.controlAttribute(new PropertyControlEvent(this, list));
        } catch (Exception e) {
            log.error("设备控制失败,事件主键为:{}---->堆栈信息:{}", request.getEvent().getId(), e);
            executeStatusEnum = ExecuteStatusEnum.FAILED_EXECUTION;
            attributeControlExecuteResultEnum = AttributeControlExecuteResultEnum.FAIL;
        }
        if (ExecuteStatusEnum.FAILED_EXECUTION == executeStatusEnum) {
            Set<Long> ids = list.stream().map(changeDeviceProEvent -> Long.valueOf(changeDeviceProEvent.getCommandSeq()))
                    .collect(Collectors.toSet());
            //批量修改执行结果为失败
            this.eventIotDeviceProRecordService.updateCommandStatus(ids, attributeControlExecuteResultEnum, StringUtils.EMPTY_STRING);
        }
        //mq消息发送
        this.sendMqMessage(request.getEvent().getId(), list);
        return executeStatusEnum;
    }

    @Override
    public void verification(LinkageActionValidRequest request) {
        if (StringUtils.isEmpty(request.getActionConfig()) || "[]".equalsIgnoreCase(request.getActionConfig())) {
            return;
        }
        //获取所有待执行的设备
        List<LinkageIotDeviceProDTO> linkageIotDeviceProDTOS = this.buildActions(request.getActionConfig());
        Assert.isNotFound(CollectionUtils.isEmpty(linkageIotDeviceProDTOS), "设备联动配置不能为空");
        linkageIotDeviceProDTOS.forEach(linkageIotDeviceProDTO -> Assert.isNotFound(
                Objects.isNull(linkageIotDeviceProDTO.getId()) ||
                StringUtils.isEmpty(linkageIotDeviceProDTO.getPropCode()) ||
                StringUtils.isEmpty(linkageIotDeviceProDTO.getPropValue()),
                "设备联动参数不能为空"));
    }

    /**
     * 设备控制
     *
     * @param param
     */
    public void deviceCtrl(DeviceCtrlCommandParam param) {
        ExecuteStatusEnum executeStatusEnum = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        AttributeControlExecuteResultEnum attributeControlExecuteResultEnum = AttributeControlExecuteResultEnum.ARRIVAL;
        //执行设备列表
        List<PropertyControlRequest> list = new ArrayList<>();
        EventIotDevicePro eventIotDevicePro = eventIotDeviceProService.getById(param.getEventIotDeviceId());
        try {
            //执行设备列表
            list = this.eventIotDeviceProService.saveAndReturnEvent(eventIotDevicePro);
            //发布一组设备执行命令到物联网平台
            this.linkageDeviceControlService.controlAttribute(new PropertyControlEvent(this, list));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeStatusEnum = ExecuteStatusEnum.FAILED_EXECUTION;
            attributeControlExecuteResultEnum = AttributeControlExecuteResultEnum.FAIL;
        }
        if (ExecuteStatusEnum.FAILED_EXECUTION == executeStatusEnum) {
            Set<Long> ids = list.stream().map(changeDeviceProEvent -> Long.valueOf(changeDeviceProEvent.getCommandSeq()))
                    .collect(Collectors.toSet());
            //批量修改执行结果为失败
            this.eventIotDeviceProRecordService.updateCommandStatus(ids, attributeControlExecuteResultEnum, StringUtils.EMPTY_STRING);
        }
        //mq消息发送
        this.sendMqMessage(eventIotDevicePro.getEventId(), list);
    }

    /**
     * 设备控制
     *
     * @param eventId
     */
    public void deviceHandCtrl(Long eventId) {
        ExecuteStatusEnum executeStatusEnum = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        AttributeControlExecuteResultEnum attributeControlExecuteResultEnum = AttributeControlExecuteResultEnum.ARRIVAL;
        //执行设备列表
        List<PropertyControlRequest> list = new ArrayList<>();
        List<EventIotDevicePro> eventIotDevicePros = eventIotDeviceProService.listByEventId(eventId);
        try {
            for (EventIotDevicePro eventIotDevicePro : eventIotDevicePros) {
                //执行设备列表
                list.addAll(this.eventIotDeviceProService.saveAndReturnEvent(eventIotDevicePro));
            }
            //发布一组设备执行命令到物联网平台
            this.linkageDeviceControlService.controlAttribute(new PropertyControlEvent(this, list));
        } catch (Exception e) {
            log.error(e.getMessage());
            executeStatusEnum = ExecuteStatusEnum.FAILED_EXECUTION;
            attributeControlExecuteResultEnum = AttributeControlExecuteResultEnum.FAIL;
        }
        if (ExecuteStatusEnum.FAILED_EXECUTION == executeStatusEnum) {
            Set<Long> ids = list.stream().map(changeDeviceProEvent -> Long.valueOf(changeDeviceProEvent.getCommandSeq()))
                    .collect(Collectors.toSet());
            //批量修改执行结果为失败
            this.eventIotDeviceProRecordService.updateCommandStatus(ids, attributeControlExecuteResultEnum, StringUtils.EMPTY_STRING);
        }
        //mq消息发送
        this.sendMqMessage(eventId, list);
    }

    /**
     * 更改执行状态
     *
     * @param commandResponse
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCommandStatus(CommandResponse commandResponse) {
        this.eventIotDeviceProRecordService.updateCommandStatus(Long.valueOf(commandResponse.getCommandSeq()), this.commandExecuteStatusMapping(commandResponse.getStatusCode()), StringUtils.EMPTY_STRING);
    }

    public static AttributeControlExecuteResultEnum commandExecuteStatusMapping(String statusCode) {
        switch (statusCode) {
            case "EXPIRED":
                return AttributeControlExecuteResultEnum.EXPIRED;
            case "SUCCESS":
                return AttributeControlExecuteResultEnum.SUCCESS;
            case "FAIL":
                return AttributeControlExecuteResultEnum.FAIL;
            case "INIT":
            case "ACCEPT":
            case "PROCESSING":
                return AttributeControlExecuteResultEnum.ISSUING;
        }

        return null;
    }

    private void sendMqMessage(Long eventId, List<PropertyControlRequest> propertySettingRequests) {
        propertySettingRequests = propertySettingRequests.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getDeviceId()))), ArrayList::new));
        RocketMQContextHolder.send(LinkageDeviceCtlMessage.TOPIC,
                                   LinkageDeviceCtlMessage
                                           .builder()
                                           .businessKey(String.valueOf(eventId))
                                           .devices(ObjectUtils.copying(propertySettingRequests, LinkageDeviceMessage.class))
                                           .build());
    }
}
