package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginRequest extends AbstractBaoLun7700Request {

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    @Override
    public String getUrl() {
        return getUrl("/api/v29+/auth")
                .append("?name={name}")
                .append("&password={password}")
                .append("&force=1").toString();
    }
}
