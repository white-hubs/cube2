package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

import java.util.List;

@Data
public class SafetyInspectStandardVO {
    private Long standardId;
    private String standardName;
    private List<SafetyInspectItemVO> itemVOList;
}
