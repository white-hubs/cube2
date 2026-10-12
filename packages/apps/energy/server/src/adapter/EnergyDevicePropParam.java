package com.guhecloud.cube.srv.gen.energymanagement.server.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyDevicePropParam {
    private String prodCategoryId;
    private Long deviceId;
    private List<String> propCodes;
    private Long startTs;
    private Long endTs;
    private String order = "desc";
    private Integer maxRecords = 1000;

    public EnergyDevicePropParam(Integer maxRecords) {
        this.maxRecords = maxRecords;
    }
}
