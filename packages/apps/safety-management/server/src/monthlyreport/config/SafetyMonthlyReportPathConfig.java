package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "safety.monthlyreport")
@Component
public class SafetyMonthlyReportPathConfig {

    /**
     * 文件路径
     */
    private String template;
}
