package com.guhecloud.cube.base.bracing.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author wql
 * @describe 消息配置参数
 * @date 2023/9/4 9:21
 */
@Data
public class MessageConfigDTO {
    /**
     * 业务类型
     */
    @NotBlank(message = "业务类型不能为空")
    private String businessType;
    /**
     * table
     */
    private List<SceneConfigDTO> sceneConfigDTOS;
}
