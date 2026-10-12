package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceAttrCtlWithPasswordCommand extends DevicePropWriteCmd {
    private String password;
}
