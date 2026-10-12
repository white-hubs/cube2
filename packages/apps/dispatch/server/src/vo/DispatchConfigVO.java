package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 调度台配置
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchConfigVO {

    //
    private Long id;
    //账号
    private String account;
    //名称
    private String name;
    //密码
    private String password;
    //平台地址
    private String platformAddr;
    //扩展配置
    private String extendConfig;

}