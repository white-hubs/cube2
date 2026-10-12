package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountCollectPO;

public interface DeviceAccountCollectCommandService extends IService<DeviceAccountCollectPO> {

    /**
     * 收藏
     *
     * @param deviceId 设备ID
     */
    void collect(Long deviceId);

    /**
     * 取消
     *
     * @param deviceId 设备ID
     */
    void cancel(Long deviceId);
}
