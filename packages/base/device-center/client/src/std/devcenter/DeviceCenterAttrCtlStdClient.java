package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePropWriteCmd;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceCenterAttrCtlStdClient {

    /**
     * 属性写值，需要存储快照日志
     * 参考原型：https://app.mockplus.cn/run/prototype/Akw7RhLooX/dfIMYGk69/kro6iinqhL9?allowShare=1&cps=expand&ha=1
     *
     * @param deviceAttrCtlCommand
     *
     * @return
     */
    @PostMapping("/device/prop/value/write")
    Long controlAttribute(@RequestBody @Valid DevicePropWriteCmd deviceAttrCtlCommand);

}
