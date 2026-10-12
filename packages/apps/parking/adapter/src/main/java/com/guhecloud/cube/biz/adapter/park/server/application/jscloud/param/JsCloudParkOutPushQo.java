package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 捷顺云开放平台 - 出场记录推送请求
 * 接口协议: 有车辆出场时，数据中心平台主动推送出场数据到第三方平台
 * 接收地址: http://serverUrl/park/parkOut
 * 请求方式: POST
 *
 * @author appet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkOutPushQo {
    /**
     * 业务参数 - 出场记录列表
     */
    @NotBlank(message = "dataItems不能为空")
    private String dataItems;
}
