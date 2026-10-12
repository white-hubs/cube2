package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.door.server.entity.DoorMonitor;
import com.guhecloud.cube.biz.door.server.param.DoorMonitorParam;
import com.guhecloud.cube.biz.door.server.vo.DoorMonitorVo;

import java.util.List;

public interface DoorMonitorService extends IService<DoorMonitor> {
    void addDoorMonitor(DoorMonitorParam doorMonitorParam);

    List<DoorMonitorVo> getDoorMonitor();

    void updateDoorMonitor(Long id, DoorMonitorParam doorMonitorParam);

    void deleteDoorMonitor(Long id);
}
