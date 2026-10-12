package com.guhecloud.cube.biz.emergency.server.initialize;

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
@ConfigurationProperties(prefix = "initdatas.emergency-type")
public class EmergencyTypeInitConfig {

    /**
     * 初始化的事件分类信息
     */
    private List<EmergencyTypeConfig> emergencyTypeConfigs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyTypeConfig {

        /**
         * 事件编号
         */
        private String code;

        /**
         * 事件名称
         */
        private String name;

        /**
         * 备注信息
         */
        private String remark;
    }
}
