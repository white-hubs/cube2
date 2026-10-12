package com.guhecloud.cube.iot.mgt;

import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.thread.IotNamedThreadFactory;
import com.guhecloud.cube.iot.mgt.comm.thread.ThreadUtil;
import com.guhecloud.cube.iot.mgt.core.data.config.ClickHouseConfigProperties;
import com.guhecloud.cube.iot.mgt.core.data.lang.env.PlatformEvn;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.cube.iot.mgt.core.impl.ClickhouseTimeSeriesImpl;
import com.guhecloud.platform.base.util.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.iot.mgt.comm.thread.ThreadUtil.DEFAULT_THREAD_SIZE;

@Configuration
public class PlatformEnvAutoConfig {

    @Bean
    public TimeSeriesInterface timeSeries(ClickHouseConfigProperties clickHouseConfigProperties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource(clickHouseConfigProperties));
        return new ClickhouseTimeSeriesImpl(jdbcTemplate);
    }

    @Bean
    @ConfigurationProperties(prefix = "click-house")
    public ClickHouseConfigProperties getClickHouseConfigProperties() {
        return new ClickHouseConfigProperties();
    }

    private HikariDataSource dataSource(ClickHouseConfigProperties clickHouseConfigProperties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(clickHouseConfigProperties.getJdbcUrl());
        hikariDataSource.setDriverClassName(clickHouseConfigProperties.getDriverClassName());
        if (StringUtils.isNotEmpty(clickHouseConfigProperties.getUsername())) {
            hikariDataSource.setUsername(clickHouseConfigProperties.getUsername());
            hikariDataSource.setPassword(clickHouseConfigProperties.getPassword());
        }
        return hikariDataSource;
    }

    /**
     * 平台环境
     *
     * @return 平台环境
     */
    @Bean
    public PlatformEvnCom platformEvnCom() {
        //目前没有运营平台，后期在拓展
        return new PlatformEvnCom(PlatformEvn.IOT_PLATFORM);
    }

    /**
     * 提供通过的IOT公共线程池<br/>
     * 此线程池不做任何任务保证,仅提供异步执行的能力
     *
     * @return 线程池
     */
    @Bean("iotCommonPool")
    public ExecutorService buildIotCommonPool() {
        return new ThreadPoolExecutor(DEFAULT_THREAD_SIZE, DEFAULT_THREAD_SIZE * 2, 60,
                                      TimeUnit.SECONDS, new LinkedBlockingQueue<>(200), new IotNamedThreadFactory("iot-common-pool", true), new ThreadUtil.DiscardPolicy());
    }
}
