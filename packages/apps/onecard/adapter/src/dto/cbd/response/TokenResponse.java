package com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenResponse {
    private String token;
    private Long expire;
}
