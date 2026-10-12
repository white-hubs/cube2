package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountPO;

import java.util.Set;

public interface DeviceAccountCommandService extends IService<DeviceAccountPO> {

    void addBatch(Set<Long> ids);

    void remove(Long id);

    void removeBatch(String ids);

    void decommissioning(Long id);

    void scrapping(Long id);

}
