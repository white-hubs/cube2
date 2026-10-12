package com.guhecloud.cube.base.device.center.open.interfaces;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceExpandMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDeviceExpand;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceAttrCtlWithPasswordCommand;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import com.guhecloud.cube.base.device.center.open.service.DeviceAttrCtlService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterAttrCtlController {

    //read only
    private final CenterBizDeviceExpandMapper centerBizDeviceExpandMapper;

    private final DeviceAttrCtlService deviceAttrCtlService;

    /**
     * 属性控制带密码校验
     *
     * @param deviceAttrCtlWithPasswordCommand 设备属性控制
     *
     * @return 此次下发的命令集合标识
     */
    @PostMapping("/device/instance/running/attr/control-with-permission")
    public Long controlAttributeWithPermission(
            @RequestBody @Valid DeviceAttrCtlWithPasswordCommand deviceAttrCtlWithPasswordCommand
                                              ) {
        Long instanceId = deviceAttrCtlWithPasswordCommand.getDeviceId();
        BizDeviceExpand deviceExpand = null;
        if (ObjectUtils.isNotEmpty(instanceId)) {
            deviceExpand = centerBizDeviceExpandMapper.selectByIdNoTenant(instanceId);
        }
        //接口兼容，如果没有密码，直接下发
        if (Objects.isNull(deviceExpand) || StringUtils.isBlank(deviceExpand.getSecurityPwd())) {
            return this.controlAttribute(deviceAttrCtlWithPasswordCommand);
        }

        // 验证密码
        if (!StringUtils.equals(deviceExpand.getSecurityPwd(), deviceAttrCtlWithPasswordCommand.getPassword())) {
            throw new ServiceException("密码不正确");
        }

        return this.controlAttribute(deviceAttrCtlWithPasswordCommand);
    }

    /**
     * 返回此次下发的命令集合标识,以供查询此次下发的命令集合执行情况
     *
     * @param devicePropWriteCmd 设备属性控制
     *
     * @return 下发命令句柄
     */
    @Deprecated
    @PostMapping("/device/instance/running/attr/control")
    public Long oldControlAttribute(@RequestBody @Valid DevicePropWriteCmd devicePropWriteCmd) {
        return deviceAttrCtlService.propCommandDown(devicePropWriteCmd);
    }

    /**
     * 属性写值，需要存储快照日志
     * 参考原型：https://app.mockplus.cn/run/prototype/Akw7RhLooX/dfIMYGk69/kro6iinqhL9?allowShare=1&cps=expand&ha=1
     *
     * @param deviceAttrCtlCommand
     *
     * @return
     */
    @PostMapping("/device/prop/value/write")
    public Long controlAttribute(@RequestBody @Valid DevicePropWriteCmd deviceAttrCtlCommand) {
        return deviceAttrCtlService.propCommandDown(deviceAttrCtlCommand);
    }
}