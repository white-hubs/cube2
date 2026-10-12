package com.guhecloud.cube.srv.gen.product.market.server.dataintegrate.util.dbbase;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "third.oracle")
public class OracleConnect {
    private String ip;
    private String port;
    private String db;
    private String username;
    private String password;
}
