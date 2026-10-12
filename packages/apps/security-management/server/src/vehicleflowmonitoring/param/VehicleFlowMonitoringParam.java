package com.guhecloud.cube.srv.gen.securitymanagement.server.vehicleflowmonitoring.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class VehicleFlowMonitoringParam extends Paging {
    private String type;
    private Period period;
}
