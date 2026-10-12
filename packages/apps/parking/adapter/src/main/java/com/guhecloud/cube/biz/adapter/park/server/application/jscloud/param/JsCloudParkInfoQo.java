package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 捷顺云开放平台 - 车场车位信息
 * 接口协议: 数据中心平台主动推送车位数据到第三方平台
 * 接收地址: http://serverUrl/park/parkSpace
 *
 * @author appet
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsCloudParkInfoQo {
    /**
     * 停车场ID，长度32
     */
    private String itemId;
    /**
     * 停车场名称，必填，长度30
     */
    @NotBlank(message = "停车场名称不能为空")
    private String parkName;
    /**
     * 停车场编号，必填，长度30
     */
    @NotBlank(message = "停车场编号不能为空")
    private String parkCode;
    /**
     * 总车位数，必填
     */
    @NotNull(message = "总车位数不能为空")
    private Integer totalSpace;
    /**
     * 剩余车位数，必填
     */
    @NotNull(message = "剩余车位数不能为空")
    private Integer remainSpace;
    /**
     * 剩余预定车位数，可选
     */
    private Integer remainBookSpace;
    /**
     * 总预定车位数，可选
     */
    private Integer totalBookSpace;
    /**
     * 备用字段，可选，长度100，json格式字符串: {"key","value"}
     */
    private String attach;
}
