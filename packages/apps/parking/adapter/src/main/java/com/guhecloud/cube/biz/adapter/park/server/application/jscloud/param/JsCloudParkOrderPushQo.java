package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 订单信息推送请求
 * dataItems 为订单列表 JSON 字符串
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkOrderPushQo {
    /**
     * 业务参数 - 订单列表（JSON 字符串）
     */
    @NotBlank(message = "dataItems不能为空")
    private String dataItems;
}

