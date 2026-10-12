package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务器配置
 *
 * @author lzy
 * @date 2021/9/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerConfiguration extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 公网IP地址
     */
    private String publicIp;

    /**
     * 公网端口
     */
    private String publicPort;

    /**
     * 内网IP地址
     */
    private String intranetIp;

    /**
     * 内网端口
     */
    private String intranetPort;

    /**
     * 公网域名地址
     */
    private String publicRealmName;

    /**
     * 公网rtmp端口
     */
    private String publicRtmpPort;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
