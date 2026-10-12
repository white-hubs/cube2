package com.guhecloud.cube.biz.door.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class StatisticsParam {
    private Period period;
    private String type;
    private String inAndOut;
    private String keyWord;
}
