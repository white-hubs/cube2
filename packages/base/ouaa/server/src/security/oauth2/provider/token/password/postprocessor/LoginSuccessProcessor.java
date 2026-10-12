package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.postprocessor;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author pengjian
 * @date 2022/10/12 16:29
 * @description
 */
public interface LoginSuccessProcessor {

    void postAfter(UserDetails userDetails);

}
