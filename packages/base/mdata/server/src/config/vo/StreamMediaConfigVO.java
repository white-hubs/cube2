package com.guhecloud.cube.biz.mdata.server.config.vo;

import lombok.Data;

/**
 * 流媒体服务器配置返回参数
 *
 * @author lanchun
 * @date 2021/12/7 15:18
 */
@Data
public class StreamMediaConfigVO {

    private Long id;

    /**
     * 流媒体名称
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 流媒体ip地址
     */
    private String ipAddress;

    /**
     * 内网地址
     */
    private String intranetAddress;
}
