package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ChangeDeviceNotifyRuleEnableCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ModifyDeviceNotifyRuleCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.ModifyDeviceNotifyRuleEnabledEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.event.ModifyDeviceNotifyRuleEvent;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRuleCommandTOEntityConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.DeviceNotifyRuleRepository;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DeviceNotifyRuleCommandService {

    private final DeviceNotifyRuleRepository deviceNotifyRuleRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public DeviceNotifyRuleCommandService(DeviceNotifyRuleRepository deviceNotifyRuleRepository,
                                          ApplicationEventPublisher applicationEventPublisher
                                         ) {
        this.deviceNotifyRuleRepository = deviceNotifyRuleRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 保存设备通知规则
     *
     * @param command 修改设备通知规则命令
     */
    public void saveDeviceNotifyRule(ModifyDeviceNotifyRuleCommand command) {
        var existDeviceNotifyRule = deviceNotifyRuleRepository.findByDeviceId(command.getDeviceId());
        var deviceNotifyRule = DeviceNotifyRuleCommandTOEntityConvert.modifyDeviceNotifyRuleCommandToEntity(command);
        deviceNotifyRuleRepository.save(deviceNotifyRule);
        applicationEventPublisher.publishEvent(new ModifyDeviceNotifyRuleEvent(this, Objects.nonNull(existDeviceNotifyRule)
                                                                                     ? existDeviceNotifyRule.getIotRuleId()
                                                                                     : null, deviceNotifyRule));
    }

    /**
     * 变更设备通知规则启用状态
     *
     * @param command 变更设备通知规则启用状态命令
     */
    public void changeDeviceNotifyRuleEnabled(ChangeDeviceNotifyRuleEnableCommand command) {
        var deviceNotifyRule = deviceNotifyRuleRepository.findByDeviceId(command.getId());
        Assert.isNotFound(deviceNotifyRule, "启用失败，请先配置");
        deviceNotifyRule.setEnabled(command.getEnabled());
        deviceNotifyRuleRepository.save(deviceNotifyRule);
        applicationEventPublisher.publishEvent(new ModifyDeviceNotifyRuleEnabledEvent(this, deviceNotifyRule));
    }
}
