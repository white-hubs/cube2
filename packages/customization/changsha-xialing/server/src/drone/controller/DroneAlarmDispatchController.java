package com.guhecloud.cube.customization.xialing.server.drone.controller;

import com.guhecloud.cube.customization.xialing.server.drone.param.DroneAlarmDispatchConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.drone.param.DroneAlarmDispatchRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneAlarmDispatchService;
import com.guhecloud.cube.customization.xialing.server.drone.vo.DroneAlarmDispatchConfigVO;
import com.guhecloud.cube.customization.xialing.server.drone.vo.DroneAlarmDispatchRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 无人机报警自动派飞配置和日志接口。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DroneAlarmDispatchController {
    private final DroneAlarmDispatchService droneAlarmDispatchService;

    /**
     * 获取当前租户唯一报警派飞配置。
     */
    @GetMapping("/drone/alarm-dispatch-config/current")
    public DroneAlarmDispatchConfigVO currentConfig() {
        return droneAlarmDispatchService.currentConfig();
    }

    /**
     * 保存当前租户唯一报警派飞配置。
     */
    @PutMapping("/drone/alarm-dispatch-config/current")
    public Boolean saveCurrentConfig(@RequestBody DroneAlarmDispatchConfigSaveParam param) {
        return droneAlarmDispatchService.saveCurrentConfig(param);
    }

    /**
     * 分页查询报警派飞日志。
     */
    @PostMapping("/drone/alarm-dispatch-record/page")
    public Pagination<DroneAlarmDispatchRecordVO> pageRecords(@RequestBody DroneAlarmDispatchRecordPageParam param) {
        return droneAlarmDispatchService.pageRecords(param);
    }

    /**
     * 查询报警派飞日志详情。
     */
    @GetMapping("/drone/alarm-dispatch-record/{id}")
    public DroneAlarmDispatchRecordVO recordDetail(@PathVariable("id") Long id) {
        return droneAlarmDispatchService.recordDetail(id);
    }
}
