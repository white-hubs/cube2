package com.guhecloud.cube.iot.mgt.transfer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.guhecloud.cube.iot.mgt.transfer.util.ProductExcelUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * 传输模块配置类
 */
@Configuration
public class TransferConfig {

    /**
     * JSON序列化配置
     */
    @Bean("transferObjectMapper")
    public ObjectMapper transferObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 禁用将日期写为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 格式化JSON输出
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return objectMapper;
    }

    /**
     * Excel处理工具Bean
     */
    @Bean
    public ProductExcelUtil productExcelUtil(ObjectMapper objectMapper) {
        return new ProductExcelUtil(objectMapper);
    }

    /**
     * 文件上传配置
     */
    @Bean("transferMultipartResolver")
    public MultipartResolver transferMultipartResolver() {
        return new StandardServletMultipartResolver();
    }
}