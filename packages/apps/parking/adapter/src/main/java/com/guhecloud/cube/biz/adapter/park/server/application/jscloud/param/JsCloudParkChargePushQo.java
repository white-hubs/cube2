package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 收费记录推送请求
 * 接收地址: http://serverUrl/park/parkCharge
 * 请求方式: POST
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkChargePushQo {
    /** 业务参数 - 收费记录列表（JSON 字符串） */
    @NotBlank(message = "dataItems不能为空")
    private String dataItems;
}
