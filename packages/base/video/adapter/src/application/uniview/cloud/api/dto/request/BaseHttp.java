package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.http.HttpClientFactory;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;

@SuperBuilder
@Slf4j
public class BaseHttp {

    private static RestTemplate REST_TEMPLATE;

    public BaseHttp() {
    }

    private static RestTemplate getRestTemplate() {
        try {
            if (REST_TEMPLATE == null) {
                REST_TEMPLATE = HttpClientFactory.createRestTemplate(ApplicationContextHolder.getBean(ObjectMapper.class));
            }
            return REST_TEMPLATE;
        } catch (GeneralSecurityException e) {
            log.error("create rest template error.", e);
        }
        return null;
    }

    public RestTemplate requester() {
        return BaseHttp.getRestTemplate();
    }
}
