package com.guhecloud.cube.biz.ouaa.server.security.oauth2.provider.token.password.postprocessor;

/**
 * @author pengjian
 * @date 2022/10/12 16:29
 * @description
 */
public interface PreCheckPostProcessor {

    void postBefore(String username);
}
