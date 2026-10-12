package com.guhecloud.cube.base.message2.server.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 变量映射详情
 *
 * @author lanchun
 * @date 2025/1/21 19:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VarMappingDetailDTO {

    /**
     * 场景类型
     */
    private String sceneType;

    /**
     * 通道
     */
    private String channelCode;

    /**
     * 厂家
     */
    private String msgManufacturer;

    /**
     * 变量映射
     */
    private Map<String, String> varMapping;
}
