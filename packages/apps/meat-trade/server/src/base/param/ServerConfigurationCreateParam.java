package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 服务器配置新增信息
 *
 * @author lzy
 * @date 2021/9/13
 */
@Data
public class ServerConfigurationCreateParam {

    /**
     * 公网IP地址
     */
    @NotEmpty(message = "公网IP地址不能为空")
    @Size(max = 100, message = "公网地址不能大于100")
    private String publicIp;

    /**
     * 公网端口
     */
    @NotEmpty(message = "公网端口")
    @Size(max = 20, message = "公网端口不能大于20")
    private String publicPort;

    /**
     * 内网IP地址
     */
    @NotEmpty(message = "内网IP地址")
    @Size(max = 100, message = "内网IP地址不能大于100")
    private String intranetIp;

    /**
     * 内网端口
     */
    @NotEmpty(message = "内网端口")
    @Size(max = 20, message = "内网端口不能大于20")
    private String intranetPort;

    /**
     * 公网域名地址
     */
    @NotEmpty(message = "公网端口")
    @Size(max = 20, message = "公网地址不能大于20")
    private String publicRealmName;

    /**
     * 公网rtmp端口
     */
    @NotEmpty(message = "公网rtmp端口")
    @Size(max = 100, message = "公网rtmp端口不能大于100")
    private String publicRtmpPort;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名")
    @Size(max = 100, message = "用户名不能大于100")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码")
    @Size(max = 100, message = "密码不能大于100")
    private String password;

}
