package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceAccountCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceAccountMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.enumeration.DeviceStatusEnum;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceAccountCommandServiceImpl extends ServiceImpl<DeviceAccountMapper, DeviceAccountPO> implements DeviceAccountCommandService {

    @Override
    public void addBatch(Set<Long> ids) {
        List<DeviceAccountPO> pos = ids.stream().map(id -> {
            DeviceAccountPO po = new DeviceAccountPO();
            po.setId(id);
            return po;
        }).collect(Collectors.toList());

        this.saveBatch(pos);
    }

    @Override
    public void remove(Long id) {
        this.removeById(id);
    }

    @Override
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

    @Override
    public void decommissioning(Long id) {
        DeviceAccountPO po = this.getById(id);
        Assert.isNotFound(po, "查不到指定设备");
        Assert.isNotAcceptable(po.getStatus() != DeviceStatusEnum.NORMAL, "该设备已经" + po.getStatus().getDescription());
        po.setStatus(DeviceStatusEnum.DECOMMISSIONING);
        this.updateById(po);
    }

    @Override
    public void scrapping(Long id) {
        DeviceAccountPO po = this.getById(id);
        Assert.isNotFound(po, "查不到指定设备");
        Assert.isNotAcceptable(po.getStatus() != DeviceStatusEnum.NORMAL, "该设备已经" + po.getStatus().getDescription());
        po.setStatus(DeviceStatusEnum.SCRAPPING);
        this.updateById(po);
    }
}
