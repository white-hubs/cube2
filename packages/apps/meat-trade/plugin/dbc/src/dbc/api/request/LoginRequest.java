package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginRequest extends AbstractDbcRequest {

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
        return getUrl("/api/v2/getToken")
                .append("?username={username}")
                .append("&password={password}").toString();
    }
}
