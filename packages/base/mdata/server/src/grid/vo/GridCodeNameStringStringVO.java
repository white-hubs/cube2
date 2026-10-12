package com.guhecloud.cube.biz.mdata.server.grid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridCodeNameStringStringVO {
    private String code;
    private String name;
}
