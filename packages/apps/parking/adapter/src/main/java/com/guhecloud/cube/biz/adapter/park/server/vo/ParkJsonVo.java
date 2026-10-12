package com.guhecloud.cube.biz.adapter.park.server.vo;

import lombok.Data;

@Data
public class ParkJsonVo {
    //车场标识
    private String parkCode;
    //车场名称
    private String parkName;
    //应用接入Code
    private String accessID;
    //接口编码
    private String apiCode;
    //企业码
    private String groupCode;
    //密钥
    private String secretKey;
    //版本
    private String version;
    //用户名
    private String userName;
    //密码
    private String password;
}
