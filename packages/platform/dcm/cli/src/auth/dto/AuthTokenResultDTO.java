package com.guhecloud.platform.dcm.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthTokenResultDTO {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String scope;
    private Long id;
    private Boolean admin;
    private String jti;
}
