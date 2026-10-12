package com.guhecloud.cube.iot.mgt.north.server.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.iot.mgt.base.model.CommandRecord;
import com.guhecloud.cube.iot.mgt.base.service.CommandInfoService;
import com.guhecloud.cube.iot.mgt.base.service.CommandLogService;
import com.guhecloud.cube.iot.mgt.base.service.CommandRecordService;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.CommandExecStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.DeviceStatusEnum;
import com.guhecloud.cube.iot.mgt.core.data.lang.cmd.DownCmd;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.CommandResetEnum;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDevicePropSetMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDeviceResetMsg;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceState;
import com.guhecloud.cube.iot.mgt.core.data.vo.device.DevicePropCacheBo;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.SimpleRule;
import com.guhecloud.cube.iot.mgt.core.data.vo.rule.TriggerType;
import com.guhecloud.cube.iot.mgt.core.def.cache.DeviceCacheSupport;
import com.guhecloud.cube.iot.mgt.core.innerbus.SouthInnerBus;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthDeviceMapper;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthProductEventMapper;
import com.guhecloud.cube.iot.mgt.north.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.north.param.CommandRestParam;
import com.guhecloud.cube.iot.mgt.north.vo.CommandRequest;
import com.guhecloud.cube.iot.mgt.north.vo.CommandResponse;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.guhecloud.cube.iot.mgt.base.model.CommandInfo.devEventResetCmdTranslatedContent;
import static com.guhecloud.cube.iot.mgt.base.model.CommandInfo.devPropResetCmdTranslatedContent;
import static com.guhecloud.cube.iot.mgt.base.model.CommandInfo.propSetCmdTranslatedContent;
import static com.guhecloud.cube.iot.mgt.base.model.CommandRecord.propSetCmdToRawContent;
import static com.guhecloud.cube.iot.mgt.base.service.CommandLogService.issueToIotPlatform;
import static com.guhecloud.cube.iot.mgt.core.data.lang.SpecialValConst.isDeviceEventThreshold;
import static com.guhecloud.cube.iot.mgt.core.data.msg.device.CommandResetEnum.RESET_DEVICE_EVENT;
import static com.guhecloud.cube.iot.mgt.core.data.msg.device.CommandResetEnum.RESET_DEVICE_PROP;

@Service
@Slf4j
public class NorthCommandService {

    @Resource
    private DeviceCacheSupport deviceCacheSupport;
    @Resource
    private SouthInnerBus southInnerBus;
    @Resource
    private NorthDeviceMapper northDeviceMapper;
    @Resource
    private NorthProductEventMapper northProductEventMapper;

    @Resource
    private CommandInfoService commandInfoService;
    @Resource
    private CommandRecordService commandRecordService;
    @Resource
    private CommandLogService commandLogService;

    public CommandResponse commandIssue(CommandRequest commandRequest) {
        DeviceDigestVo deviceDigestVo = queryDeviceDigest(commandRequest.getDeviceId());

        //事件阈值下发不进行属性检查，后期可调整为直接属性下发，在产品属性中暴露阈值设定
        if (!isDeviceEventThreshold(commandRequest.getPropCode())) {
            DevicePropCacheBo bo = northDeviceMapper.queryDevicePropByIdAndCode(Long.parseLong(commandRequest.getDeviceId()), commandRequest.getPropCode());
            if (Objects.isNull(bo)) {
                throw new ServiceException("属性不存在");
            }

            if (bo.getDataWrite() == 0) {
                throw new ServiceException("属性为只读，不支持下发");
            }
        }

        String commandSeq = buildCommandSeqIfNeed(commandRequest.getCommandSeq());

        // command wrap
        CommandResponse commandResponse = new CommandResponse();
        commandResponse.setCommandSeq(commandSeq);
        commandResponse.setCommandHandle(commandSeq);
        commandResponse.setMessage("执行中");
        commandResponse.setStatusCode(CommandExecStatusEnum.PROCESSING.code);

        //build cmd record
        String translatedContent = propSetCmdTranslatedContent(
                deviceDigestVo.getDeviceName(),
                commandRequest.getPropCode(),
                commandRequest.getPropValue());
        String rawCmdContent = propSetCmdToRawContent(
                deviceDigestVo.getDeviceId(),
                commandRequest.getPropCode(),
                commandRequest.getPropValue());

        commandInfoService.startCmd(commandSeq, DownCmd.DEV_PROP_SET, translatedContent);
        commandRecordService.startCmdRecord(commandSeq, commandSeq, translatedContent, rawCmdContent, Long.valueOf(deviceDigestVo.getDeviceId()), commandRequest.getPropCode());
        commandLogService.logCmd(commandSeq, commandSeq, issueToIotPlatform);

        //build down msg
        PreDevicePropSetMsg preDevicePropSetMsg = new PreDevicePropSetMsg();
        preDevicePropSetMsg.setCommandSeq(commandSeq);
        preDevicePropSetMsg.setTenant(deviceDigestVo.getTenantId());
        preDevicePropSetMsg.setDeviceId(commandRequest.getDeviceId());
        preDevicePropSetMsg.setDeviceName(deviceDigestVo.getDeviceName());
        preDevicePropSetMsg.setDeviceNumber(deviceDigestVo.getDeviceNumber());
        preDevicePropSetMsg.setInstanceId(commandSeq);//通常由业务平台生成
        preDevicePropSetMsg.setParentDeviceId(deviceDigestVo.getParentDeviceId());
        preDevicePropSetMsg.setParentDeviceNumber(deviceDigestVo.getParentDeviceNumber());
        preDevicePropSetMsg.setPropCode(commandRequest.getPropCode());
        preDevicePropSetMsg.setPropValue(commandRequest.getPropValue()); //regulateValue by PreDevicePropSetMsgHandler
        preDevicePropSetMsg.setTs(System.currentTimeMillis());
        log.info("preDevicePropSetMsg:{}", JSON.toJSONString(preDevicePropSetMsg));
        southInnerBus.process(preDevicePropSetMsg);

        //处理设备缓存信息
        return commandResponse;
    }

    public CommandResponse commandStatus(String instanceId) {
        Assert.isTrue(StringUtils.isNotBlank(instanceId), "commandHandle 不能为空");
        CommandRecord commandRecord = commandRecordService.queryCommandRecordByInstanceId(instanceId);

        if (Objects.nonNull(commandRecord)) {
            CommandResponse commandResponse = new CommandResponse();
            commandResponse.setCommandHandle(instanceId);
            commandResponse.setCommandSeq(instanceId);
            commandResponse.setStatusCode(commandRecord.getSingleCmdStatus());
            commandResponse.setMessage(commandRecord.getResultTips());

            return commandResponse;
        }

        CommandResponse commandResponse = new CommandResponse();
        commandResponse.setCommandHandle(instanceId);
        commandResponse.setCommandSeq("-1");
        commandResponse.setStatusCode(CommandExecStatusEnum.ERROR.code);
        commandResponse.setMessage("系统没有执行此命令");
        return commandResponse;
    }

    private String buildCommandSeqIfNeed(String commandSeq) {
        return StringUtils.isBlank(commandSeq) ? String.valueOf(IdWorker.getId()) : commandSeq;
    }

    /**
     * 设备重置命令下发
     *
     * @param restParam restParam
     *
     * @return CommandResponse
     */
    public CommandResponse commandDeviceResetIssue(CommandRestParam restParam) {
        DeviceDigestVo deviceDigestVo = queryDeviceDigest(restParam.getDeviceId());

        String commandSeq = buildCommandSeqIfNeed(restParam.getCommandSeq());

        switch (restParam.getCommandResetEnum()) {
            case RESET_DEVICE_EVENT:
                //重置设备事件，这里需要检查一下事件规格，如果是通过平台属性模拟出来的事件，那么应该还原为属性重置
                MdProductEvent prodEvent = northProductEventMapper.queryProductEventDesignatedCode(deviceDigestVo.getProdId(), restParam.getResetTarget());

                if (Objects.isNull(prodEvent)) {
                    return buildCommandResponse(commandSeq, CommandExecStatusEnum.FAIL.code, "产品事件[" + restParam.getResetTarget() + "]不存在, 不能重置事件");
                }

                //目前事件仅支持简单规则，基于单个属性判断，默认仅处理一个属性code
                EventConfig eventConfig = prodEvent.getConfig();

                if (eventConfig == null || eventConfig.getTriggerType() == null) {
                    return buildCommandResponse(commandSeq, CommandExecStatusEnum.FAIL.code, "EventConfig invalid");
                }

                TriggerType triggerType = eventConfig.getTriggerType();
                if (triggerType == TriggerType.SIMPLE && eventConfig.getRules() instanceof SimpleRule simpleRule) {
                    String propCode = simpleRule.getPropCode();
                    DevicePropCacheBo devicePropCacheBo = northDeviceMapper.queryDevicePropByIdAndCode(Long.valueOf(deviceDigestVo.getDeviceId()), propCode);

                    buildAndIssueDeviceRestMsg(deviceDigestVo, commandSeq, RESET_DEVICE_PROP, propCode, devicePropCacheBo.getDefaultValue());
                } else {
                    buildAndIssueDeviceRestMsg(deviceDigestVo, commandSeq, RESET_DEVICE_EVENT, restParam.getResetTarget(), null);
                }
                break;
            case RESET_DEVICE_PROP:
                DevicePropCacheBo devicePropCacheBo = northDeviceMapper.queryDevicePropByIdAndCode(Long.valueOf(deviceDigestVo.getDeviceId()), restParam.getResetTarget());
                if (Objects.isNull(devicePropCacheBo)) {
                    return buildCommandResponse(commandSeq, CommandExecStatusEnum.FAIL.code, "设备属性[" + restParam.getResetTarget() + "]不存在, 不能重置属性");
                }
                //build down msg
                buildAndIssueDeviceRestMsg(deviceDigestVo, commandSeq, restParam.getCommandResetEnum(), restParam.getResetTarget(), devicePropCacheBo.getDefaultValue());
                break;
        }

        // command wrap
        return buildCommandResponse(commandSeq, CommandExecStatusEnum.PROCESSING.code, "执行中");
    }

    private void buildAndIssueDeviceRestMsg(DeviceDigestVo deviceDigestVo, String commandSeq,
                                            CommandResetEnum resetEnum, String resetTarget, Object resetValue
                                           ) {
        PreDeviceResetMsg preDeviceResetMsg = PreDeviceResetMsg.builder()
                .commandSeq(commandSeq)
                .instanceId(commandSeq)
                .tenantId(deviceDigestVo.getTenantId())
                .deviceId(deviceDigestVo.getDeviceId())
                .deviceName(deviceDigestVo.getDeviceName())
                .prodId(deviceDigestVo.getProdId())
                .protocolId(deviceDigestVo.getProtocolId())
                .commandResetEnum(resetEnum)
                .resetTarget(resetTarget)
                .resetValue(resetValue)
                .deviceNumber(deviceDigestVo.getDeviceNumber())
                .parentDeviceId(deviceDigestVo.getParentDeviceId())
                .parentDeviceNumber(deviceDigestVo.getParentDeviceNumber())
                .build();

        // build cmd record
        boolean isResetDeviceEvent = (resetEnum == CommandResetEnum.RESET_DEVICE_EVENT);
        String translatedContent = buildTranslatedContent(isResetDeviceEvent, deviceDigestVo.getDeviceName(), resetTarget, resetValue);

        commandInfoService.startCmd(commandSeq, isResetDeviceEvent ? DownCmd.DEV_EVENT_RESET : DownCmd.DEV_PROP_RESET, translatedContent);
        commandRecordService.startCmdRecord(commandSeq, commandSeq, translatedContent, JSON.toJSONString(preDeviceResetMsg), Long.valueOf(deviceDigestVo.getDeviceId()), resetTarget);
        commandLogService.logCmd(commandSeq, commandSeq, issueToIotPlatform);

        log.info("commandDeviceResetIssue:{}", JSON.toJSONString(preDeviceResetMsg));
        southInnerBus.process(preDeviceResetMsg);
    }

    private String buildTranslatedContent(boolean isResetDeviceEvent, String deviceName, String resetTarget, Object resetValue) {
        if (isResetDeviceEvent) {
            return devEventResetCmdTranslatedContent(deviceName, resetTarget);
        }
        return devPropResetCmdTranslatedContent(deviceName, resetTarget, resetValue);
    }

    private DeviceDigestVo queryDeviceDigest(String restParam) {
        DeviceDigestVo deviceDigestVo = northDeviceMapper.queryDeviceCache(Long.valueOf(restParam));
        Optional<DeviceState> deviceStateOptional = deviceCacheSupport.queryDeviceStateCache(Long.valueOf(restParam));
        if (!deviceStateOptional.isPresent()) {
            log.error("CoreDeviceSpecialService.handleRemoteThirdPartyIdBind: deviceDigestCacheBo is null");
            throw new ServiceException("设备[" + restParam + "] 未找到,或者未激活");
        }

        DeviceState deviceState = deviceStateOptional.get();

        if (deviceState.getStatus() == DeviceStatusEnum.NO_ACTIVE.value
            || deviceState.getStatus() == DeviceStatusEnum.DISABLED.value) {
            throw new ServiceException("设备[" + restParam + "] 未激活或则被禁用");
        }
        return deviceDigestVo;
    }

    private CommandResponse buildCommandResponse(String commandSeq, String statusCode, String message) {
        CommandResponse commandResponse = new CommandResponse();
        commandResponse.setCommandSeq(commandSeq);
        commandResponse.setCommandHandle(commandSeq);
        commandResponse.setStatusCode(statusCode);
        commandResponse.setMessage(message);
        return commandResponse;
    }
}
