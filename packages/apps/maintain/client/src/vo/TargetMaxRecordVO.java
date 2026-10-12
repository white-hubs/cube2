package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetMaxRecordVO {
    private Long targetId;
    private Integer count;
    private String item;
    private String tag;
}
