package com.guhecloud.platform.dcm.client;

import com.guhecloud.platform.dcm.config.DcmConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import(DcmConfiguration.class)
public class TestConfiguration {
}
