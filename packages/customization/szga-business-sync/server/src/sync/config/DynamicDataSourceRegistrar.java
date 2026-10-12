package com.guhecloud.cube.srv.gen.business.info.sync.server.sync.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.dto.DataDictionaryDTO;
import com.guhecloud.platform.base.util.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/22 17:47
 * @Description:
 */
@Component
@Slf4j
public class DynamicDataSourceRegistrar {
    public static final List<String> DATA_SOURCE_LIST = Arrays.asList("SOURCE_DATABASE", "TARGET_DATABASE");

    private static final String DATA_SYNC_DATASOURCE = "DATA_SYNC_DATASOURCE";

    private final ConfigurableApplicationContext applicationContext;
    private final RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean dataSourceCreated = false;

    public DynamicDataSourceRegistrar(ConfigurableApplicationContext applicationContext, RedisTemplate<String, Object> redisTemplate) {
        this.applicationContext = applicationContext;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取或创建数据源
     */
    public HikariDataSource getOrCreateDataSource(String code) {
        // 先尝试从容器中获取
        String dataSourceName = StringUtils.toCamelCase(code);
        if (applicationContext.containsBean(dataSourceName)) {
            return applicationContext.getBean(dataSourceName, HikariDataSource.class);
        }
        // 双重检查锁确保线程安全
        if (!dataSourceCreated) {
            synchronized (this) {
                if (!dataSourceCreated) {
                    // 创建并注册数据源
                    HikariDataSource dataSource = createAndRegisterDataSource(code);
                    dataSourceCreated = true;
                    return dataSource;
                }
            }
        }
        return applicationContext.getBean(dataSourceName, HikariDataSource.class);
    }

    private HikariDataSource createAndRegisterDataSource(String code) {
        try {
            DataDictionaryDTO dataDictionaryVO = this.get(DATA_SYNC_DATASOURCE, code, DataDictionaryDTO.class);
            if (Objects.isNull(dataDictionaryVO) || StringUtils.isBlank(dataDictionaryVO.getVariable())) {
                return null;
            }
            Map<String, String> paramMap = objectMapper.readValue(dataDictionaryVO.getVariable(), new TypeReference<Map<String, String>>() {
            });
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(paramMap.get("jdbcUrl"));
            config.setUsername(paramMap.get("userName"));
            config.setPassword(paramMap.get("password"));
            config.setDriverClassName(paramMap.get("driverClassName"));
            config.setPoolName(paramMap.get("poolName"));
            config.setMaximumPoolSize(20);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.setAutoCommit(false);
            // 注册到Spring容器
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
            // 创建 BeanDefinition
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class, () -> hikariDataSource);
            registry.registerBeanDefinition(StringUtils.toCamelCase(code), builder.getBeanDefinition());
            return hikariDataSource;
        } catch (Exception exception) {
            log.error("初始化数据库连接池失败{}", code, exception);
        }
        return null;
    }

    public <T> T get(String itemCode, String code, Class<T> clazz) throws JsonProcessingException {
        Object value = this.redisTemplate.opsForValue().get(this.getRedisKey(itemCode, code));
        if (Objects.isNull(value)) {
            return null;
        }
        return objectMapper.readValue(value.toString(), clazz);
    }

    public String getRedisKey(String itemCode, String code) {
        return "DICTIONARY:" + itemCode + ":" + 0 + ":" + code;
    }

    /**
     * 检查数据源是否可用
     */
    public boolean checkDataSourceAvailable() {
        boolean result = false;
        for (String key : DATA_SOURCE_LIST) {
            try {
                HikariDataSource dataSource = getOrCreateDataSource(key);
                try (Connection conn = dataSource.getConnection()) {
                    result = conn.isValid(2);
                }
            } catch (SQLException e) {
                return false;
            }
            dataSourceCreated = false;
        }
        return result;
    }
}
