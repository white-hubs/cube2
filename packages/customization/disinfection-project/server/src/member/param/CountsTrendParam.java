package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class CountsTrendParam {
    public String productTypeCode;
    public Period period;
}
