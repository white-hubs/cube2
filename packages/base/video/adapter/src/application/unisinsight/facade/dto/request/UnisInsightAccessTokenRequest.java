package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnisInsightAccessTokenRequest extends AbstractRequest {

    private String grantType;

    private String clientId;

    private String clientSecret;

    private String format;

    @JsonIgnore
    public Map<String, Object> getParamForGet() {
        Map<String, Object> param = new HashMap<>(4);
        param.put("grant_type", grantType);
        param.put("client_id", clientId);
        param.put("client_secret", clientSecret);
        param.put("format", format);
        return param;
    }

    @Override
    public void addCustomHttpHeaders(org.springframework.http.HttpHeaders headers) {

    }

    @Override
    public String getUrl() {
        return getUrl("/sso/oauth2.0/accessToken")
                .append("?grant_type={grant_type}")
                .append("&client_id={client_id}")
                .append("&client_secret={client_secret}")
                .append("&format={format}")
                .build();
    }
}
