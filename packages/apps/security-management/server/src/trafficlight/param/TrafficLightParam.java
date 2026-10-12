package com.guhecloud.cube.srv.gen.securitymanagement.server.trafficlight.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class TrafficLightParam extends Paging {
    private Period period;
}
