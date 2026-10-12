package com.guhecloud.cube.biz.adapter.park.server.application.jslife.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JSLifeResponse {
    private Integer resultCode;
    private String message;

}
