package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/8/3 15:59
 * @description
 */
@Data
public class DeviceInstanceReliabilityQuery {
    @NotEmpty
    private Set<Long> deviceIds;
    @NotNull
    private Period period;
}
