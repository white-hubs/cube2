package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.DeviceMonitorMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.DeviceMonitor;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceMonitorParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.DeviceMonitorService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceMonitorVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceMonitorServiceImpl extends ServiceImpl<DeviceMonitorMapper, DeviceMonitor> implements DeviceMonitorService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDeviceMonitor(DeviceMonitorParam param) {
        DeviceMonitor one = this.getOne(Wrappers.<DeviceMonitor>lambdaQuery()
                                                .eq(DeviceMonitor::getMonitorName, param.getMonitorName())
                                                .eq(DeviceMonitor::getApplicationCode, param.getApplicationCode()));
        Assert.isConflict(Objects.nonNull(one), "该监控信息已存在");
        DeviceMonitor copying = ObjectUtils.copying(param, DeviceMonitor.class);
        this.save(copying);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeviceMonitor(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceMonitor(Long id, DeviceMonitorParam param) {
        DeviceMonitor byId = this.getById(id);
        byId.setMonitorName(param.getMonitorName());
        byId.setMonitorUrl(param.getMonitorUrl());
        this.updateById(byId);
    }

    @Override
    public List<DeviceMonitorVo> getDeviceMonitor(String applicationCode) {
        List<DeviceMonitor> list = this.list(Wrappers.<DeviceMonitor>lambdaQuery()
                                                     .eq(DeviceMonitor::getApplicationCode, applicationCode));
        return ObjectUtils.copying(list, DeviceMonitorVo.class);
    }
}
