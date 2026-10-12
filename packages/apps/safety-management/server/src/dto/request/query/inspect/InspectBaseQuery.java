package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.inspect;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.InspectionTaskStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectBaseQuery extends BaseStatisticsQuery {
    private List<InspectionTaskStatusEnum> status;
}
