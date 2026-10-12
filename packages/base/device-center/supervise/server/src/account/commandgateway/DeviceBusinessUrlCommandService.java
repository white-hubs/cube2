package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.command.DeviceBusinessUrlModifyParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceBusinessUrlPO;

public interface DeviceBusinessUrlCommandService extends IService<DeviceBusinessUrlPO> {

    Long save(DeviceBusinessUrlModifyParam param);

    void update(Long id, DeviceBusinessUrlModifyParam param);
}
