package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 捷顺云开放平台 - 车位信息推送请求
 * 接口协议: 数据中心平台主动推送车位数据到第三方平台
 * 接收地址: http://serverUrl/park/parkSpace
 * 请求方式: POST
 *
 * @author appet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkInfoPushQo {
    /**
     * 业务参数 - 车场车位信息列表
     */
    @NotBlank(message = "dataItems不能为空")
    private String dataItems;
}
