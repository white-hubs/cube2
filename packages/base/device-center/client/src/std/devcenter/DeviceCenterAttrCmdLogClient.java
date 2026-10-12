package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceInstanceExecuteCommandVO;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceInstanceExecuteCommandQuery;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

@FeignClient(CLIENT_NAME)
public interface DeviceCenterAttrCmdLogClient {

    @PostMapping("/device/instance/attribute/change/log/page/post")
    Pagination<DeviceInstanceExecuteCommandVO> pageChangeLogPost(@RequestBody DeviceInstanceExecuteCommandQuery query);
}
