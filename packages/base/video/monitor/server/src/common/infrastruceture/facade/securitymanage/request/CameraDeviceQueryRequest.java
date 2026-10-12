package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDeviceQueryRequest {

    private String keyword;
    private List<String> areaIds;
    private String areaId;
    private String status;
    private String genre;
    private List<Long> deviceIds;
}
