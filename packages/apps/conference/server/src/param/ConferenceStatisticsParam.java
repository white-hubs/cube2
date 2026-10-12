package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class ConferenceStatisticsParam {
    private Period period;
    private String type;
}
