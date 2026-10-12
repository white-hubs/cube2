package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.interfaces.vo.DeviceLifeEventsVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceLifeEventsPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.query.DeviceLifeEventsQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface DeviceLifeEventsQueryService extends IService<DeviceLifeEventsPO> {

    Pagination<DeviceLifeEventsVO> getPage(DeviceLifeEventsQuery query);

    Map<String, String> getEventTypeMap();

}
