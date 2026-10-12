package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InspectionItemStatisticsQuery extends Paging {
    private Period period;
    private String type;
    private List<String> tagList;
    private Integer marketTenant;
    List<Integer> marketTenantList;
}
