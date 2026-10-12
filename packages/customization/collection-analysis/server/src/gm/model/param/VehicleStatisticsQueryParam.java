package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleStatisticsQueryParam extends TrendQueryPageBaseParam {

    private String vehicleNature;

    private String day;
}
