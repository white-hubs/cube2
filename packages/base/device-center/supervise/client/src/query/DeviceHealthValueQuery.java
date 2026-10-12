package com.guhecloud.cube.srv.gen.device.supervise.client.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHealthValueQuery {
    private Set<Long> deviceIds;
}
