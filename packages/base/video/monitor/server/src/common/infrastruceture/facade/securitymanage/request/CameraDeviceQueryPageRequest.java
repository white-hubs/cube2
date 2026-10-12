package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.securitymanage.request;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDeviceQueryPageRequest extends Paging {

    private String keyword;
    private List<String> areaIds;
    private String areaId;
    private Boolean deviceStatus;
    private String genre;
    private List<java.lang.Long> deviceIds;
    private List<String> tagCodes;
}
