package com.guhecloud.cube.srv.gen.energymanagement.server.config;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxdbClientConfig {
    @Value("${influx.url}")
    private String url;

    @Value("${influx.token}")
    private String token;

    @Value("${influx.dborg}")
    private String dborg;

    @Bean("influxdbClient")
    InfluxDBClient createInfluxdbClient() {
        System.out.println("influx链接URL:" + url);
        System.out.println("influx访问token:" + token);
        // 生成客户端
        InfluxDBClient client = InfluxDBClientFactory.create(url, token.toCharArray(), dborg);
        client.setLogLevel(LogLevel.BASIC);
        return client;
    }
}
