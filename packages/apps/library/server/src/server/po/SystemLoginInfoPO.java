package com.guhecloud.cube.srv.gen.library.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统登录信息
 */
@Data
@TableName("system_login_info")
@EqualsAndHashCode(callSuper = true)
public class SystemLoginInfoPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = -8014040615275609074L;
    private Long id;
    private String sceneCode;
    private String baseUrl;
    private String userId;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private LocalDateTime tokenExpires;
    private String refreshToken;
    private String remark;
}
