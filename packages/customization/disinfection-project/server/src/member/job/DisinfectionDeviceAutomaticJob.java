package com.guhecloud.cube.srv.gen.disinfection.server.member.job;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterAttrCtlStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.disinfection.server.member.dao.DisinfectionDeviceAutomaticMapper;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.member.event.DisinfectionMemberLogAddEvent;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePropDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionDeviceAutomaticJob {
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DeviceCenterAttrCtlStdClient deviceCenterAttrCtlStdClient;
    private final DisinfectionDeviceAutomaticMapper disinfectionDeviceAutomaticMapper;

    public void memberDeviceAutomatic(String param) {
        Assert.isParameterError(StringUtils.isBlank(param), "指令id为空");
        var automatic = disinfectionDeviceAutomaticMapper.selectById(Long.parseLong(param));
        Assert.isNotFound(automatic, "指令不存在：" + param);
        SessionContextHolder.requestTenant(automatic.getTenant());
        /* 组装指令*/
        var deviceAttrCtlCommand = new DevicePropWriteCmd();
        deviceAttrCtlCommand.setDeviceId(automatic.getDeviceId());
        Set<DevicePropWriteCmd.PropWriteValue> propWriteValues = new HashSet<>();
        var automaticPropDTOS = JSONArray.parseArray(automatic.getPropWriteValueList(), MemberDevicePropDTO.class);
        var stringBuffer = new StringBuffer();
        Optional.ofNullable(automaticPropDTOS).orElse(new ArrayList<>()).forEach(dto -> {
            var propWriteValue = new DevicePropWriteCmd.PropWriteValue();
            propWriteValue.setCode(dto.getCode());
            propWriteValue.setExpectedValue(dto.getExpectedValue());
            propWriteValues.add(propWriteValue);
            stringBuffer.append(dto.getName()).append(":").append(dto.getExpectedName()).append(",");
        });
        deviceAttrCtlCommand.setPropWriteValues(propWriteValues);
        // 执行
        try {
            deviceCenterAttrCtlStdClient.controlAttribute(deviceAttrCtlCommand);
        } catch (Exception e) {
            log.error("自动化指令执行失败:{}", e.getMessage(), e);
        }

        var deviceDto = deviceCenterStdClient.getDeviceById(automatic.getDeviceId());
        var tital = "执行了";
        var detail = automatic.getName();
        if (deviceDto != null && deviceDto.getBaseArchivesInfo() != null) {
            tital = deviceDto.getBaseArchivesInfo().getName();
            detail = deviceDto.getBaseArchivesInfo().getName();
        }
        if (!stringBuffer.isEmpty()) {
            var str = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
            detail = detail + ":" + str;
        }
        /* 发送日志事件 */
        var logAddEvent = new DisinfectionMemberLogAddEvent(this, automatic.getCreateBy(), MemberLogTypeEnum.DEVICE_WORK_LOG, automatic.getDeviceId(), tital, detail);
        applicationEventPublisher.publishEvent(logAddEvent);
    }
}
