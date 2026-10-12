package com.guhecloud.cube.biz.subsystem.broadcast.server.init.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "initdatas.brocast-template")
public class BroadcastTemplateInit {

    /**
     * 模板集合
     */
    private List<BroadcastTemplateConfig> templates;
}
