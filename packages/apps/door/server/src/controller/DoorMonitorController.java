package com.guhecloud.cube.biz.door.server.controller;

import com.guhecloud.cube.biz.door.server.param.DoorMonitorParam;
import com.guhecloud.cube.biz.door.server.service.DoorMonitorService;
import com.guhecloud.cube.biz.door.server.vo.DoorMonitorVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lrj
 * @Description 门禁控制
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorMonitorController {
    private final DoorMonitorService doorMonitorService;

    /**
     * @Description 新增门禁监控信息
     * @Author lrj
     */
    @PostMapping("/addDoorMonitor")
    public void addDoorMonitor(@RequestBody DoorMonitorParam doorMonitorParam) {
        doorMonitorService.addDoorMonitor(doorMonitorParam);
    }

    /**
     * @Description 门禁监控信息列表
     * @Author lrj
     */
    @PostMapping("/getDoorMonitor")
    public List<DoorMonitorVo> getDoorMonitor() {
        return doorMonitorService.getDoorMonitor();
    }

    /**
     * @Description 门禁监控信息修改
     * @Author lrj
     */
    @PutMapping("/updateDoorMonitor/{id}")
    public void updateDoorMonitor(@PathVariable Long id, @RequestBody DoorMonitorParam doorMonitorParam) {
        doorMonitorService.updateDoorMonitor(id, doorMonitorParam);
    }

    /**
     * @Description 门禁监控信息删除
     * @Author lrj
     */
    @DeleteMapping("/deleteDoorMonitor/{id}")
    public void deleteDoorMonitor(@PathVariable Long id) {
        doorMonitorService.deleteDoorMonitor(id);
    }

}
