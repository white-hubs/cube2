package com.guhecloud.cube.biz.ouaa.server.service;

import com.guhecloud.cube.biz.ouaa.server.dto.AuthInfoCacheDTO;
import com.guhecloud.cube.biz.ouaa.server.param.SystemUserAuthParam;

public interface SystemUserAuthService {

    String systemUserAuth(SystemUserAuthParam param);

    AuthInfoCacheDTO getAuthInfo(String tokenId);

    void removeAuthInfo(String tokenId);
}
