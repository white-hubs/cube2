package com.guhecloud.cube.srv.gen.business.info.sync.server.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/21 13:52
 * @Description:
 */
@Configuration
public class CustomBeanConfig {

    @Bean
    public WebClient webClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("business_sync")
                .maxConnections(10)
                .maxIdleTime(Duration.ofSeconds(10))
                .build();
        HttpClient httpClient = getHttpClient(connectionProvider);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    /**
     * httpclient配置
     *
     * @param connectionProvider
     *
     * @return
     */
    private static HttpClient getHttpClient(ConnectionProvider connectionProvider) {
        TcpClient tcpClient = TcpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn ->
                                       conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                               .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                              );
        return HttpClient.from(tcpClient)
                .responseTimeout(Duration.ofSeconds(5));
    }
}
