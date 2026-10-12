package com.guhecloud.cube.biz.ouaa.server.service.login;

import com.guhecloud.cube.biz.ouaa.server.entity.User;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-05  10:12
 * @Description:
 * @Version: 1.0
 */
public interface MSOauthService {
    User getAccessToken(String code, String redirectUri);
}
