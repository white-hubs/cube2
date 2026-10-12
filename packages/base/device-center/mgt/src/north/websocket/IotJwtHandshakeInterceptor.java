package com.guhecloud.cube.iot.mgt.north.websocket;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class IotJwtHandshakeInterceptor implements HandshakeInterceptor {

    public static final String PARAM_TERMINAL = "terminal";
    public static final String ATTR_LOGIN_USER = "loginUser";
    public static final String ATTR_TERMINAL_USER = "terminalUser";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_TENANT = "tenant";
    @Autowired
    JwtDecoder jwtDecoder;

    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return queryPairs;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes
                                  ) throws Exception {
        Map<String, String> params = splitQuery(request.getURI().getQuery());
        String token = params.get(PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        if (Objects.isNull(params.get(PARAM_TENANT))) {
            return false;
        }
        //租户
        Integer tenant = Integer.valueOf(params.get(PARAM_TENANT));
        //访问端
        String terminal = params.get(PARAM_TERMINAL);
        if (StringUtils.isEmpty(terminal)) {
            return false;
        }
        Requester requester = Requester.builder()
                .tenant(tenant)
                .terminal(terminal)
                .build();
        SessionContextHolder.setRequester(requester);
        Jwt jwt = jwtDecoder.decode(token);
        if (Objects.nonNull(jwt)) {
            Long userId = TokenUtils.getId(jwt);
            if (Objects.nonNull(userId)) {
                attributes.put(ATTR_LOGIN_USER, tenant + ":" + userId);
                attributes.put(ATTR_TERMINAL_USER, terminal + ":" + userId);
                attributes.putAll(params);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
