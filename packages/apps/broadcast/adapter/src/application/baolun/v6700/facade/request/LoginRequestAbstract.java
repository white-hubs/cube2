package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestAbstract extends AbstractBasicRequest {

    /**
     * 用户名称
     */
    private String User;

    /**
     * 密码
     */
    private String Passwd;

    @Override
    public String getUrl() {
        return getUrl("login").toString();
    }

}
