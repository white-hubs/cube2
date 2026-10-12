package com.guhecloud.platform.dcm.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ouaa.auth")
public class AuthTokenProperties {
    private String url;

    private String clientId;

    private String clientSecret;

    private String terminal;

    private Long tenant;

    private Integer refreshInterval = 300;

    @Override
    public String toString() {
        return "AuthTokenProperties{" +
               "url='" + url + '\'' +
               ", clientId='" + clientId + '\'' +
               ", clientSecret='" + encloseSecretContent(clientSecret) + '\'' +
               ", terminal='" + terminal + '\'' +
               ", tenant=" + tenant +
               ", refreshInterval=" + refreshInterval +
               '}';
    }

    private String encloseSecretContent(String origin) {
        if (origin == null || origin.length() == 0) {
            return origin;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(origin.charAt(0));
        for (int i = origin.length(); i > 1; --i) {
            sb.append('*');
        }
        return sb.toString();
    }
}
