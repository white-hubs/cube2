package com.guhecloud.cube.biz.adapter.park.server.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChargeTotalRequest extends BasicRequest {

    /**
     * 日期(要求格式为yyyyMMdd)
     */
    private String date;

    @Override
    public String getUrl() {
        return getUrl("").toString();
    }
}
