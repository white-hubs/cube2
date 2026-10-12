package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class TargetMaxRecordParam {
    private List<Long> targetIdList;
    private List<String> tagList;
    private Period period;
}
