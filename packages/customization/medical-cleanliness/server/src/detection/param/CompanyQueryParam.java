package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyQueryParam extends Paging {
    private Integer tenantId;
}
