package com.guhecloud.cube.biz.meattrade.server.third.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Slf4j
public abstract class AbstractThirdRequest extends AbstractRequest {

    @JsonIgnore
    public String getToken() {
        return SecurityContextHolder.getJwt().getTokenValue();
    }

    public HttpHeaders getCustomHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(token)) {
            headers.set("Authorization", "bearer " + token);
        }
        headers.set("Tenant", SessionContextHolder.getRequester().getTenant().toString());
        headers.set("terminal", SessionContextHolder.getRequester().getTerminal());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
