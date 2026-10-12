package com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class FlowFormDataQueryParam extends Paging {

    private Period period;

}
