package com.guhecloud.cube.srv.gen.securitymanagement.server.common.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.DeviceMonitorService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceMonitorVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonitorManagementController {

    private final DeviceMonitorService deviceMonitorService;

    /**
     * @Description 添加组态
     * @Author lrj
     */
    @PostMapping("/addDeviceMonitor")
    public void addDeviceMonitor(@RequestBody DeviceMonitorParam param) {
        deviceMonitorService.addDeviceMonitor(param);
    }

    /**
     * @Description 删除组态
     * @Author lrj
     */
    @DeleteMapping("/deleteDeviceMonitor/{id}")
    public void deleteDeviceMonitor(@PathVariable Long id) {
        deviceMonitorService.deleteDeviceMonitor(id);
    }

    /**
     * @Description 编辑组态
     * @Author lrj
     */
    @PutMapping("/updateDeviceMonitor/{id}")
    public void updateDeviceMonitor(@PathVariable Long id, @RequestBody DeviceMonitorParam param) {
        deviceMonitorService.updateDeviceMonitor(id, param);
    }

    /**
     * @Description 获取组态信息
     * @Author lrj
     */
    @GetMapping("/getDeviceMonitor/{applicationCode}")
    public List<DeviceMonitorVo> getDeviceMonitor(@PathVariable String applicationCode) {
        return deviceMonitorService.getDeviceMonitor(applicationCode);
    }

}
