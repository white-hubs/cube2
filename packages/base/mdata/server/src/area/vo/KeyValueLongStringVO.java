package com.guhecloud.cube.biz.mdata.server.area.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueLongStringVO {
    private Long key;
    private String value;
}
