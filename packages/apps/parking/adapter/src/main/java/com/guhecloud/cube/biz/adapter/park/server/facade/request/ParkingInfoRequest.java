package com.guhecloud.cube.biz.adapter.park.server.facade.request;

import lombok.Data;

@Data
public class ParkingInfoRequest extends BasicRequest {

    @Override
    public String getUrl() {
        return getUrl("").toString();
    }
}
