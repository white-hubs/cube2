package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class RiskSourceDeviceQueryParam {

    private Period period;

    private List<String> deviceNameList;
}
