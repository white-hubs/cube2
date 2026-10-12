package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedLogErrorVO {
    private Integer errorRow;
    private String errorMsg;
}
