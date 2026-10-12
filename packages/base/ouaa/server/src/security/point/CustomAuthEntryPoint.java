package com.guhecloud.cube.biz.ouaa.server.security.point;

import com.guhecloud.cube.biz.ouaa.server.security.exceptions.LoginException;
import com.guhecloud.cube.biz.ouaa.server.security.response.LoginErrorResponse;
import com.guhecloud.platform.base.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
                        ) throws IOException {
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String errorCode = "AUTH_FAILED";
        Object details = "AUTH_FAILED";
        if (authException instanceof LoginException ex) {
            status = ex.getHttpStatus();
            errorCode = ex.getErrorCode();
            details = ex.getDetails();
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().write(JsonUtils.serialize(LoginErrorResponse.of(status, authException.getMessage(), errorCode, details)));
    }
}
