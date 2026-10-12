package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectRecordPageParam extends Paging {

    /**
     * 标签code集合
     */
    private Set<String> codes;

    /**
     * 时间范围
     */
    private Period period;
}
