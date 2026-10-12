package com.guhecloud.cube.biz.dispatch.server.map.config;//package com.guhecloud.cube.biz.dispatch.server.map.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gaode")
public class GaoDeConfig {

    private String appKey;

    private String prvSecret;
}
