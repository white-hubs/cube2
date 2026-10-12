package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetMaxRecordResult {
    private Long targetId;
    private String tag;
    private Integer count;
    private String item;
}
