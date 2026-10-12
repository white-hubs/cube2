package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.DeviceCollection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceCollectionParam;
import org.springframework.web.bind.annotation.RequestBody;

public interface DeviceCollectionService extends IService<DeviceCollection> {
    void addDeviceCollection(@RequestBody DeviceCollectionParam param);
}
