package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class DeviceLifeEventsQuery extends Paging {

    private Long deviceId;

    private String eventType;

    private Period period;

}
