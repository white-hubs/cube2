package com.guhecloud.cube.biz.mdata.server.config.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频流媒体配置
 *
 * @author lanchun
 * @date 2021/12/7 15:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StreamMediaConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 流媒体名称
     */
    @Unique(composites = { "deleted" }, message = "名称已存在")
    private String name;

    /**
     * 流媒体ip地址
     */
    private String ipAddress;

    /**
     * 内网地址
     */
    private String intranetAddress;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密后的密码
     */
    private String encryptPassword;
}
