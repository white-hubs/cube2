package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param;

import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectRecordItemTagCodeStatParam extends StatisticsQueryParam {

    /**
     * 标签code集合
     */
    private Set<String> codes;
}
