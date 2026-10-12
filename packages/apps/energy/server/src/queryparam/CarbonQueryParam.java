package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import lombok.Data;

@Data
public class CarbonQueryParam {
    private String timeFlag;
    private String period;
    private Boolean isCarbon;
}
