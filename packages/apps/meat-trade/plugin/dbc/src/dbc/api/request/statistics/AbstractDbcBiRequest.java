package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.config.DbcPorkArrangePushConfig;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.AbstractRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.LoginRequest;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

public abstract class AbstractDbcBiRequest extends AbstractRequest {

    private static final LoginRequest LOGIN_REQUEST = new LoginRequest();

    public HttpHeaders getCustomHttpHeaders() {
        return LOGIN_REQUEST.getCustomHttpHeaders();
    }

    @JsonIgnore
    public DbcPorkArrangePushConfig getRequestConfig() {
        DbcPorkArrangePushConfig requestConfig = super.getRequestConfig();
        if (Objects.isNull(requestConfig)) {
            return null;
        }
        requestConfig.setServerApiAddr(requestConfig.getServerBiApiAddr());
        return requestConfig;
    }
}
