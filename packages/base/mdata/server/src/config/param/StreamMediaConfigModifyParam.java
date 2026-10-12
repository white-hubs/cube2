package com.guhecloud.cube.biz.mdata.server.config.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 流媒体服务器配置保存参数
 *
 * @author lanchun
 * @date 2021/12/7 15:11
 */
@Data
public class StreamMediaConfigModifyParam {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * ip地址
     */
    @NotEmpty(message = "ip地址不能为空")
    @Size(max = 100, message = "ip地址不能大于100")
    private String ipAddress;

    /**
     * 内网地址
     */
    @Size(max = 100, message = "内网地址不能大于100")
    private String intranetAddress;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Size(max = 16, message = "用户名不能大于16")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Size(max = 20, message = "密码不能大于20")
    private String password;
}
