package com.guhecloud.cube.biz.linkage.server.facade.iot.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyControlResponse {

    private String commandSeq;
    private String commandHandle;
    private String statusCode;
    private String message;
}
