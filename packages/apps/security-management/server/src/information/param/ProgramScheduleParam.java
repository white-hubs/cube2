package com.guhecloud.cube.srv.gen.securitymanagement.server.information.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class ProgramScheduleParam extends Paging {
    private String clientId;
    private String clientName;

    private Period period;
}
