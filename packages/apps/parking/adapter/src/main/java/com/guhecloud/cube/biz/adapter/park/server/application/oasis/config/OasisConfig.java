package com.guhecloud.cube.biz.adapter.park.server.application.oasis.config;

import lombok.Data;

import java.util.List;

@Data
public class OasisConfig {
    private String tokenBaseUrl;
    private String apiBaseUrl;
    private String appId = "datauser";
    private List<String> parkingIds;
    private Long tokenTtlSeconds = 300L;
}
