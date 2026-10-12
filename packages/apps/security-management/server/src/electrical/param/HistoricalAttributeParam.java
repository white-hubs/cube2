package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param;

import lombok.Data;

import java.util.List;

@Data
public class HistoricalAttributeParam {
    private Long deviceId;
    private String period;
    private List<String> attributives;
}
