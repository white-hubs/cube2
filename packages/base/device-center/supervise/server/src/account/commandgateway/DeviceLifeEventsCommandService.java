package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message.DeviceLifeEventDelMessage;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.event.message.DeviceLifeEventMessage;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceLifeEventsPO;

public interface DeviceLifeEventsCommandService extends IService<DeviceLifeEventsPO> {

    void saveOrUpdateEvents(DeviceLifeEventMessage message);

    void delEvent(DeviceLifeEventDelMessage message);
}
