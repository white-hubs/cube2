package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyLoginRequest extends AbstractGhyRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @Override
    public String getUrl() {
        return getUrl("/api/user/login")
                .append("?username={username}")
                .append("&password={password}").toString();
    }
}
