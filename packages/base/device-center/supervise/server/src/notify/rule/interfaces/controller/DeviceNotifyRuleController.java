package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.controller;

import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.DeviceNotifyRuleCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ChangeDeviceNotifyRuleEnableCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.commandgateway.command.ModifyDeviceNotifyRuleCommand;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.DeviceNotifyRuleQueryService;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.query.DeviceNotifyRuleQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRulePageVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceNotifyRuleController {

    private final DeviceNotifyRuleCommandService deviceNotifyRuleCommandService;

    private final DeviceNotifyRuleQueryService deviceNotifyRuleQueryService;

    /**
     * 保存设备通知规则
     *
     * @param command 修改设备通知规则命令
     */
    @PostMapping("/device-notify-rule")
    public void saveDeviceNotifyRule(@RequestBody ModifyDeviceNotifyRuleCommand command) {
        deviceNotifyRuleCommandService.saveDeviceNotifyRule(command);
    }

    /**
     * 变更设备通知规则启用状态
     *
     * @param command 变更设备通知规则启用状态命令
     */
    @PutMapping("/device-notify-rule/enable")
    public void changeDeviceNotifyRule(@RequestBody ChangeDeviceNotifyRuleEnableCommand command) {
        deviceNotifyRuleCommandService.changeDeviceNotifyRuleEnabled(command);
    }

    /**
     * 获取设备通知规则
     *
     * @param deviceId 设备ID
     *
     * @return 设备通知规则VO
     */
    @GetMapping("/device-notify-rule/{deviceId}")
    public DeviceNotifyRuleVO getDeviceNotifyRule(@PathVariable("deviceId") Long deviceId) {
        return deviceNotifyRuleQueryService.getRuleDetailByDeviceId(deviceId);
    }

    /**
     * 分页查询设备通知规则
     *
     * @param query 查询参数
     *
     * @return 设备通知规则分页VO
     */
    @GetMapping("/device-notify-rules")
    public Pagination<DeviceNotifyRulePageVO> page(DeviceNotifyRuleQuery query) {
        return deviceNotifyRuleQueryService.page(query);
    }
}
