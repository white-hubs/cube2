package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param;

import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.enumeration.EnvironmentalDataTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateParam extends Paging {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Period period;
    private EnvironmentalDataTypeEnum dataTypeEnum;
}
