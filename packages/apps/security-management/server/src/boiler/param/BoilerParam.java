package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoilerParam extends Paging {
    private LocalDateTime stTime;
    private List<Long> deviceIds;
    private Period period;
    private String keyword;
}
