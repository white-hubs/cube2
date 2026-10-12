package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DetectionReportQueryParam extends Paging {
    private Integer tenant;
    private Period period;
}
