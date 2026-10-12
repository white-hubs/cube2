package com.guhecloud.cube.biz.ouaa.server.security.invalidation;

public interface TokenInvalidation {

    void removeToken(String account, String terminal);
}
