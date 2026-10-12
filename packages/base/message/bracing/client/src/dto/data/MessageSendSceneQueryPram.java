package com.guhecloud.cube.base.bracing.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author wql
 * @describe 消息发送场景查询参数
 * @date 2023/9/5 15:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MessageSendSceneQueryPram {
    /**
     * 业务类型(多个可用逗号拼接)
     */
    @NotBlank(message = "业务类型不能为空")
    private String businessType;
    /**
     * 场景码
     */
    private List<String> sceneCodes;
}
