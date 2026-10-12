package com.guhecloud.cube.biz.linkage.server.facade.device.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkageVideoResponse {

    private String thirdpartyId;
    private String name;
    private Long id;
    private String deviceCode;
}
