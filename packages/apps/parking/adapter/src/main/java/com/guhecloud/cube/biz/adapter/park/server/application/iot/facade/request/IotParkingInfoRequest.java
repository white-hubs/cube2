package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.request;

import lombok.Data;

@Data
public class IotParkingInfoRequest extends IotBasicRequest {

    private String tenant;

    private Integer projectId;

    private String prodCode;

    private Boolean withSnapShot;

    private Boolean withProperty;

    public IotParkingInfoRequest(String tenant, Integer projectId, String prodCode, Boolean withSnapShot, Boolean withProperty) {
        this.tenant = tenant;
        this.projectId = projectId;
        this.prodCode = prodCode;
        this.withSnapShot = withSnapShot;
        this.withProperty = withProperty;
    }

    @Override
    public String getUrl() {
        return getUrl("project")
                .append(projectId.toString())
                .append("device")
                .append("?prodCode={prodCode}")
                .append("&withProperty={withProperty}")
                .append("&withSnapShot={withSnapShot}")
                .build();

    }
}
