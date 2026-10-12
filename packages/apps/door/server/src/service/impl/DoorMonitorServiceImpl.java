package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.server.dao.DoorMonitorMapper;
import com.guhecloud.cube.biz.door.server.entity.DoorMonitor;
import com.guhecloud.cube.biz.door.server.param.DoorMonitorParam;
import com.guhecloud.cube.biz.door.server.service.DoorMonitorService;
import com.guhecloud.cube.biz.door.server.vo.DoorMonitorVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorMonitorServiceImpl extends ServiceImpl<DoorMonitorMapper, DoorMonitor> implements DoorMonitorService {
    @Override
    public void addDoorMonitor(DoorMonitorParam doorMonitorParam) {
        DoorMonitor doorMonitor = new DoorMonitor();
        ObjectUtils.copying(doorMonitorParam, doorMonitor);
        this.save(doorMonitor);
    }

    @Override
    public List<DoorMonitorVo> getDoorMonitor() {
        List<DoorMonitor> list = this.list();
        if (!list.isEmpty()) {
            return ObjectUtils.copying(list, DoorMonitorVo.class);
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public void updateDoorMonitor(Long id, DoorMonitorParam doorMonitorParam) {
        DoorMonitor byId = this.getById(id);
        Assert.isNotFound(byId == null, "无修改对象");
        ObjectUtils.copying(doorMonitorParam, byId);
        this.updateById(byId);
    }

    @Override
    public void deleteDoorMonitor(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
