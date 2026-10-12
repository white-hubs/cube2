package com.guhecloud.cube.iot.mgt.core.data.north.outparam;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebSocketParam extends OutParam {

    private String clientId;

    public WebSocketParam(String clientId) {
        this.clientId = clientId;
    }

    public WebSocketParam() {
    }

    @Override
    protected String identityDo() {
        return clientId;
    }

    public static WebSocketParam build(String clientId, String tenant) {
        WebSocketParam webSocketParam = new WebSocketParam(clientId);
        webSocketParam.setTenant(tenant);
        return webSocketParam;
    }
}
