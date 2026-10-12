package com.guhecloud.cube.base.dataintegrate.server.param;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Appet
 * @Description 采集市场配置
 * @date 2023-09-09 15:08:36
 */
@Data
public class MarketConfigParam {

    //市场名称
    @NotNull(message = "市场名称不能为空")
    private String marketName;
    //市场编号
    @NotNull(message = "市场编号不能为空")
    private String marketCode;
    //市场方租户号
    @Max(value = 999999999, message = "租户编号只能小于10位数")
    private Integer marketTenant;
    //请求地址
    private String url;
    //登录账号
    private String account;
    //登录密码
    private String password;
    //坐标
    private String coordinate;
    //地址
    private String address;
    //创建时间
    private String foundTime;
    //市场图片
    private String fileIds;//多个用逗号分割
    //市场面积
    private Double marketArea;
    //是否开启
    private Boolean status;

}