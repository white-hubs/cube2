package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request;

import lombok.Data;

@Data
public class H5sVideoLoginRequest extends BasicRequest {

    /**
     * 用户
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    @Override
    public String getUrl() {
        return getUrl("/api/v1/Login")
                .append("?user={user}")
                .append("&password={password}")
                .build();
    }
}
