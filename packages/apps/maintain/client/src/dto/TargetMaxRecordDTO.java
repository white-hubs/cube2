package com.guhecloud.cube.srv.gen.maintain.dto;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class TargetMaxRecordDTO {
    private List<Long> targetIdList;
    private List<String> tagList;
    private Period period;
}
