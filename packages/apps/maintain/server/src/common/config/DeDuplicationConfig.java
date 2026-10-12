package com.guhecloud.cube.srv.gen.maintain.server.common.config;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author pengjian
 * @date 2022/9/19 10:10
 * @description 去重配置
 */
@Data
public class DeDuplicationConfig {
    private Long id;
    /**
     * 去重周期
     */
    private Integer deDuplicationPeriod;
    /**
     * 持续时间
     */
    private Integer duration;

    /**
     * 开关设置
     * true: 开
     * false: 关
     */
    @NotNull
    private Boolean switchSetting;
    @NotBlank
    private String businessType;
}
