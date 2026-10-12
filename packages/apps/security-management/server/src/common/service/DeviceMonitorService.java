package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.DeviceMonitor;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceMonitorVo;

import java.util.List;

public interface DeviceMonitorService extends IService<DeviceMonitor> {
    void addDeviceMonitor(DeviceMonitorParam param);

    void deleteDeviceMonitor(Long id);

    void updateDeviceMonitor(Long id, DeviceMonitorParam param);

    List<DeviceMonitorVo> getDeviceMonitor(String applicationCode);
}
