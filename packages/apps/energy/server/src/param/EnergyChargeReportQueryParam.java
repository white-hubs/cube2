package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 费用报表查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyChargeReportQueryParam extends TrendQueryPageBaseParam {
    private Long id;

    private Long areaId;
    private List<String> instrumentTypeList;
}
