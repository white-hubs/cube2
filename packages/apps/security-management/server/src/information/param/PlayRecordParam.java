package com.guhecloud.cube.srv.gen.securitymanagement.server.information.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class PlayRecordParam extends Paging {
    private String keyword;
    private String area;
    private Period period;
}
